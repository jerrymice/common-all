package org.open.code.base.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * @author tumingjian
 * 说明:可以通过
 * http://服务器地址/user/runCrontabTask.json?jobName=任务中文名
 * 或者
 * 来立即运行一个定时任务
 */
@Slf4j
public class TaskProvider {
    @Autowired
    protected TaskService taskService;
    @Autowired
    private TaskAuthPassword taskAuthPassword;
    @Autowired
    private TaskAuthToken taskAuthToken;
    @Autowired
    private TaskProcessor failure;

    private Map<String, Method> targetMethods = new HashMap<>();

    @PostConstruct
    public void init() {
        /**
         * 如果目标service是一个aop代理类,那么获取定时任务的真实目标类
         */
        Class<?> targetClass;
        if (taskService != null) {
            targetClass = taskService.getClass();
            while (true) {
                try {
                    Method getTargetClass = taskService.getClass().getMethod("getTargetClass");
                    targetClass = (Class) getTargetClass.invoke(taskService, null);
                } catch (Exception e) {
                    break;
                }
            }
        }else{
            targetClass=null;
        }
        /**
         * 查找所有定时任务方法
         */
        if (targetClass != null) {
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                Task task;
                if ((task = method.getAnnotation(Task.class)) != null) {
                    String taskName = task.value();
                    targetMethods.put(taskName, method);
                }
            }
        }
    }

    /**
     * 定时任务对外暴露的接口.
     *
     * @param jobName  为了兼容老接口的参数,与taskName一样.为要调用的任务名
     * @param taskName 任务名称
     * @param username 用户名
     * @param password 密码
     * @param token    token
     * @param txid     运行时.如果定时调度服务器支持,那么会传入一个txid,每一个任务的每一次调用都会有一个唯一的txid.
     * @return 如果认证失败, 返回认证失败信息, 如果认证成功, 返回任务执行的信息.
     */
    public Object executeTask(@Deprecated String jobName, String taskName, String username, String password, String token, String txid) {
        /**
         * 用户认证.支持token  username,password中任一种.
         */
        boolean verifyResult;
        /**
         * 为txid设置默认的值
         */
        txid = txid == null ? "local_temp_" + System.currentTimeMillis() : txid;
        if (token != null) {
            verifyResult = taskAuthToken.verify(token);
        } else {
            verifyResult = taskAuthPassword.verify(username, password);
        }
        log.debug(MessageFormat.format("txid:{0},user verify result:{1}", txid, verifyResult));
        /**
         * 验证失败时返回的结果处理
         */
        if (!verifyResult) {
            if (token != null) {
                return failure.verifyFailure(token);
            } else {
                return failure.verifyFailure(username);
            }
        }
        /**
         * 任务名为空时返回结果处理
         */
        taskName = taskName == null ? jobName : taskName;
        if (taskName == null) {
            return failure.taskNameIsNull();
        }
        /**
         * 没有定时任务实现类时的返回结果处理
         */
        if (taskService == null) {
            return failure.notFoundTask(taskName, txid);
        }
        /**
         * 查找定时任务方法
         */
        Method method = targetMethods.get(taskName);
        Object result;
        if (method != null) {
            Long startTime = System.currentTimeMillis();
            log.debug(MessageFormat.format("txid:{0},taskName:{1},定时任务开始执行,时间:{2}", txid, taskName, String.valueOf(startTime)));
            try {
                method.setAccessible(true);
                Parameter[] parameters = method.getParameters();
                Object[] args = new Object[parameters.length];
                int i = 0;
                if (parameters != null && parameters.length > 1) {
                    for (Parameter parameter : parameters) {
                        if (parameter.getName().equals("txid") && parameter.getType() == String.class) {
                            String s = new String(txid);
                            args[i] = s;
                        } else {
                            args[i] = null;
                        }
                        i++;
                    }
                    result = method.invoke(taskService, args);
                } else {
                    result = method.invoke(taskService, args);
                }
            } catch (Exception e) {
                log.error(MessageFormat.format("txid:{0},taskName:{1},定时任务执行失败",txid,taskName),e);
                result = failure.taskExeError(taskName, txid, e);
            }
            log.debug(MessageFormat.format("txid:{0},taskName:{1},定时任务结束执行,时间:{2}", txid, taskName, String.valueOf(System.currentTimeMillis())));
            return failure.taskExeSuccess(taskName,txid,result);
        } else {
            return failure.notFoundTask(taskName, txid);
        }
    }
}
