package com.github.jerrymice.common.entity.ex;

/**
 * @author tumingjian
 * 创建时间: 2019-10-04 11:44
 * 功能说明: 当远程调用返回Result的code码不为0000时,将直接抛出一个这个远程业务异常,由框架抛出.
 * 通常情况下不要抛出这个异常,这个异常仅仅是做为RPC调用失败时抛出.如果本地业务异常应该抛出ResultException
 */
public class RemoteResultException extends ResultException {
    private int httpStatus;
    private String url;
    private String request;
    private String response;

    /**
     * 获取远程API地址
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * 获取远程请求内容
     * @return
     */
    public String getRequest() {
        return request;
    }

    /**
     * 获取远程请求响应内容
     * @return
     */
    public String getResponse() {
        return response;
    }

    /**
     * 构造函数
     * @param code 远程代码
     * @param message 远程消息
     * @param url  远程API URL
     * @param request 请求地址
     * @param response 响应内容
     */
    public RemoteResultException(int httpStatus,String code,String message,String url, String request, String response) {
        super(code,message);
        this.httpStatus=httpStatus;
        this.url = url;
        this.request = request;
        this.response = response;
    }
    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getUrl()!=null?",url:"+getUrl()+","+getLocalizedMessage():getLocalizedMessage();
        return (message != null) ? (s + ": " + "[" + getCode() + "]" + message) : (s + ": " + "[" + getCode() + "]");
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
