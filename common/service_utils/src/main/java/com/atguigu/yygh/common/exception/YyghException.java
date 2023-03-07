package com.atguigu.yygh.common.exception;

public class YyghException extends RuntimeException {

    private Integer code;
    private String message;

    public YyghException() {
    }

    public YyghException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取
     * @return code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置
     * @param code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "YyghException{code = " + code + ", message = " + message + "}";
    }
}
