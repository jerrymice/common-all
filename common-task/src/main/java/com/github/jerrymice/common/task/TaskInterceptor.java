package com.github.jerrymice.common.task;



import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author tumingjian
 * 说明: 定时任务拦截器.他的优先级应该在登录拦截器之前
 */
public class TaskInterceptor implements HandlerInterceptor {
    /**
     * 任务提供者
     */
    private TaskProvider taskProvider;
    /**
     * json序列化者
     */
    private HttpMessageConverter<Object> converter;

    /**
     *
     * @param taskProvider  task任务的提供者
     * @param converter  spring mvc HttpMessageConverter对象
     */
    public TaskInterceptor(TaskProvider taskProvider, HttpMessageConverter<Object> converter) {
        this.taskProvider = taskProvider;
        this.converter = converter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jobName = request.getParameter("jobName");
        String taskName = request.getParameter("taskName");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String token = request.getParameter("token");
        String txid = request.getParameter("txid");
        Object o = taskProvider.executeTask(jobName, taskName, username, password, token, txid);
        ServletServerHttpResponse serverHttpResponse = new ServletServerHttpResponse(response);
        converter.write(o, MediaType.APPLICATION_JSON, serverHttpResponse);
        /**
         * 阻止流程调用一下个拦截器
         */
        return false;
    }

}
