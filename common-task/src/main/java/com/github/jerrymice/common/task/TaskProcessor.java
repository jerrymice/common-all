package com.github.jerrymice.common.task;


import com.github.jerrymice.common.entity.code.GlobalErrorCode;
import com.github.jerrymice.common.entity.entity.Result;
import com.github.jerrymice.common.entity.entity.ResultInfo;

/**
 * @author tumingjian
 * <p>
 * 说明:定时任务结果处理者
 */
public interface TaskProcessor {
    /**
     * taskName参数或jobName参数为空时的返回结果
     *
     * @return 可以是任何对象
     */
    default Object taskNameIsNull() {
        return new ResultInfo(TaskErrorCode.TASK_NAME_NOT_EMPTY);
    }

    /**
     * 验证失败时的返回结果
     *
     * @param value 这个值会是用户名或token值
     * @return 可以是任何对象
     */
    default Object verifyFailure(String value) {
        return new ResultInfo(TaskErrorCode.TASK_USER_AUTH_ERROR.format(value));
    }

    /**
     * 找不到指定的任务名时的返回结果
     *
     * @param taskName 任务名称
     * @param txid     唯一的该定任务执行ID
     * @return 可以是任何对象
     */
    default Object notFoundTask(String taskName, String txid) {
        return new ResultInfo(TaskErrorCode.TASK_NOT_FOUND.format(taskName, txid));
    }

    /**
     * 任务执行异常时的返回信息
     *
     * @param taskName 任务名称
     * @param txid     唯一的该定任务执行ID
     * @param e        异常信息
     * @return 可以是任何对象
     */
    default Object taskExeError(String taskName, String txid, Exception e) {
        return new ResultInfo(TaskErrorCode.TASK_EXECUTE_ERROR.format(taskName, txid, e.getLocalizedMessage()));
    }

    /**
     * 任务执行成功时的信息.
     *
     * @param taskName 任务名称
     * @param txid     唯一的该定任务执行ID
     * @param result   任务执行成功后的结果
     * @return 可以是任何对象
     */
    default Object taskExeSuccess(String taskName, String txid, Object result) {
        if (result instanceof Result) {
            return result;
        } else {
            return new ResultInfo<>(true).setBody(result);
        }
    }

    /**
     * 必传参数为空时的处理信息
     *
     * @param taskName  任务名称
     * @param txid      任务
     * @param paramName 参数名
     * @return
     */
    default Object paramsIsNull(String taskName, String txid, String paramName) {
        return new ResultInfo<>(TaskErrorCode.TASK_INVALID_ARGUMENTS.format(taskName,txid,paramName));
    }

    /**
     * 任务超时时处理.
     * @param taskName 定时任务
     * @param txid  唯一的该定任务执行ID
     * @return
     */
    default Object executeTimeout(String taskName,String txid){
        return new ResultInfo<>(TaskErrorCode.TASK_EXECUTE_TIMEOUT.format(taskName,txid));
    }
}
