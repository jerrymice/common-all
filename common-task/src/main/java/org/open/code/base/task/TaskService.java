package org.open.code.base.task;

import java.util.HashMap;

/**
 * @author tumingjian
 *
 * 说明:作TaskService层的接口,但目前一个Web服务只能有一个类实现该接口,多个的话会产生冲突
 */
public interface TaskService {
    /**
     * 这是一个默认的测试任务
     * @return  返回一个与ResultInfo的对象
     */
    @Task(value = "测试任务")
    default Object testTask(){
        HashMap<String, Object> result = new HashMap<>(4);
        result.put("success", true);
        result.put("code", null);
        result.put("message", "测试任务执行成功");
        result.put("object", null);
        return result;
    }
}
