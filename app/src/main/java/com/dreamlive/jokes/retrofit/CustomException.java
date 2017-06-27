package com.dreamlive.jokes.retrofit;


/**
 * 自定义异常-1失败，1成功
 */
public class CustomException extends RuntimeException {
    /**
     * 异常代码 -1服务器处理异常 会含有异常消息 -2未登录 token验证失败
     */
    private int code;
    /**
     * 服务器处理异常 异常消息
     */
    private String msg;

    /**
     * @param code 异常代码 -1服务器处理异常 会含有异常消息 -2未登录 token验证失败
     * @param msg  服务器处理异常 异常消息
     */
    public CustomException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * @param code 异常代码 -1服务器处理异常 会含有异常消息 -2未登录 token验证失败
     * @param msg  服务器处理异常 异常消息
     */
    public CustomException(String detailMessage, int code, String msg) {
        super(detailMessage);
        this.code = code;
        this.msg = msg;
    }

    /**
     * @param code 异常代码 -1服务器处理异常 会含有异常消息 -2未登录 token验证失败
     * @param msg  服务器处理异常 异常消息
     */
    public CustomException(Throwable throwable, int code, String msg) {
        super(throwable);
        this.code = code;
        this.msg = msg;
    }

    /**
     * @param code 异常代码 -1服务器处理异常 会含有异常消息 -2未登录 token验证失败
     * @param msg  服务器处理异常 异常消息
     */
    public CustomException(String detailMessage, Throwable throwable, int code, String msg) {
        super(detailMessage, throwable);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 异常代码 -1服务器处理异常 会含有异常消息 -2未登录 token验证失败
     */
    public int getCode() {
        return code;
    }

    /**
     * 服务器处理异常 异常消息
     */
    public String getMsg() {
        return msg;
    }
}
