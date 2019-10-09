package com.github.jerrymice.common.task;


import com.github.jerrymice.common.entity.ex.ResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


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
        } else {
            targetClass = null;
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
     * @param jobName       为了兼容老接口的参数,与taskName一样.为要调用的任务名
     * @param taskName      任务名称
     * @param username      用户名
     * @param password      密码
     * @param token         token
     * @param requestParams 其他额外参数
     * @return 如果认证失败, 返回认证失败信息, 如果认证成功, 返回任务执行的信息.
     */
    public Object executeTask(@Deprecated String jobName, String taskName, String username, String password, String token, Map<String, Object> requestParams) {
        /**
         * 用户认证.支持token  username,password中任一种.
         */
        boolean verifyResult;
        /**
         * 为txid设置默认的值
         */
        String txid = Optional.ofNullable(requestParams).map(i -> i.get("txid")).map(i -> i.toString()).orElse("local_temp_" + System.currentTimeMillis());
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
                Object[] args = null;
                Parameter[] parameters = method.getParameters();
                if (parameters != null && parameters.length >= 1) {
                    Map<String, Object> map = convertParameter(taskName, txid, requestParams, parameters);
                    /**
                     * 如果参数验证出错,那么直接返回
                     */
                    if (map.get("error") != null) {
                        return map.get("error");
                    } else {
                        args = (Object[])map.get("args");
                    }
                }
                Object invoke = method.invoke(taskService, args);
                log.info(MessageFormat.format("txid:{0},taskName:{1},定时任务执行成功", txid, taskName));
                return invoke;
            } catch (Exception e) {
                log.error(MessageFormat.format("txid:{0},taskName:{1},定时任务执行失败", txid, taskName), e);
                result = failure.taskExeError(taskName, txid, e);
            }
            log.debug(MessageFormat.format("txid:{0},taskName:{1},定时任务结束执行,时间:{2}", txid, taskName, String.valueOf(System.currentTimeMillis())));
            return failure.taskExeSuccess(taskName, txid, result);
        } else {
            return failure.notFoundTask(taskName, txid);
        }
    }

    private Map<String, Object> convertParameter(String taskName, String txid, Map<String, Object> requestParams, Parameter[] parameters) {
        HashMap<String, Object> result = new HashMap<>();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            RequestParam annotation = parameter.getAnnotation(RequestParam.class);
            /**
             * 优先取value值,再取name值.
             */
            DefaultConversionService conversionService = new DefaultConversionService();
            String paramName = annotation != null ? annotation.value() != null ? annotation.value() : annotation.name() : null;
            if (paramName != null && requestParams.get(paramName) != null) {
                if (parameter.getType() == String.class) {
                    args[i] = requestParams.get(paramName);
                } else {
                    boolean canConvert = conversionService.canConvert(String.class, parameter.getType());
                    /**
                     * 如果参数能够转换成功,那么直接转换,如果不能转换成功那么就报错.
                     */
                    if (canConvert) {
                        Object value = conversionService.convert(requestParams.get(paramName), parameter.getType());
                        args[i] = value;
                    }
                }
            }
            /**
             * 必传参数为空时返回信息
             */
            if (args[i] == null && annotation != null && annotation.required()) {
                log.error(TaskErrorCode.TASK_INVALID_ARGUMENTS.format(taskName, txid, paramName).getMessage());
                result.put("error", failure.paramsIsNull(taskName, txid, paramName));
                return result;
            }
        }
        result.put("args", args);
        return result;
    }
}
