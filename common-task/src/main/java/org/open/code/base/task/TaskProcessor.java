package org.open.code.base.task;


import com.github.jerrymice.common.entity.code.ErrorCode;
import com.github.jerrymice.common.entity.entity.ResultInfo;

/**
 * @author tumingjian
 *
 * 说明:定时任务结果处理者
 */
public interface TaskProcessor {
    /**
     * taskName参数或jobName参数为空时的返回结果
     *
     * @return  可以是任何对象
     */
    default Object taskNameIsNull() {
        return new ResultInfo(false).setCode(ErrorCode.TASK_NAME_NOT_EMPTY.getCode()).setMessage(ErrorCode.TASK_NAME_NOT_EMPTY.getMessage());
    }

    /**
     * 验证失败时的返回结果
     *
     * @param value  这个值会是用户名或token值
     * @return 可以是任何对象
     */
    default Object verifyFailure(String value) {
        return new ResultInfo(false).setCode(ErrorCode.TASK_USER_AUTH_ERROR.getCode()).setMessage(ErrorCode.TASK_USER_AUTH_ERROR.getMessage(value));
    }

    /**
     * 找不到指定的任务名时的返回结果
     *
     * @param taskName 任务名称
     * @param txid 唯一的该定任务执行ID
     * @return 可以是任何对象
     */
    default Object notFoundTask(String taskName, String txid) {
        return new ResultInfo(false).setCode(ErrorCode.TASK_NOT_FOUND.getCode()).setMessage(ErrorCode.TASK_NOT_FOUND.getMessage(taskName,txid));
    }

    /**
     * 任务执行异常时的返回信息
     *
     * @param taskName 任务名称
     * @param txid 唯一的该定任务执行ID
     * @param e  异常信息
     * @return 可以是任何对象
     */
    default Object taskExeError(String taskName, String txid, Exception e) {
        return new ResultInfo(false).setCode(ErrorCode.TASK_EXECUTE_ERROR.getCode()).setMessage(ErrorCode.TASK_EXECUTE_ERROR.getMessage(taskName,txid, e.getMessage()));
    }

    /**
     * 任务执行成功时的信息.
     * @param taskName 任务名称
     * @param txid  唯一的该定任务执行ID
     * @param result  任务执行成功后的结果
     * @return 可以是任何对象
     */
    default Object taskExeSuccess(String taskName, String txid, Object result){
        return new ResultInfo<>(true).setObject(result);
    }

}
