package com.atguigu.yygh.common.result;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


@ToString
@Getter
public class R {


    private Integer code;
    private Boolean success;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    private R() {
    }

    public R(Integer code, Boolean success, String message, Map<String, Object> data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static R ok() {
        R r = new R();
        r.code = REnum.SUCCESS.getCode();
        r.success = REnum.SUCCESS.getFlag();
        r.message = REnum.SUCCESS.getMessage();
        return r;
    }

    public static R error() {
        R r = new R();
        r.code = REnum.ERROR.getCode();
        r.success = REnum.ERROR.getFlag();
        r.message = REnum.ERROR.getMessage();
        return r;
    }

    public R code(Integer code) {
        this.code = code;
        return this;
    }

    public R success(Boolean success) {
        this.success = success;
        return this;
    }

    public R message(String message) {
        this.message = message;
        return this;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public R map(Map<String, Object> map) {
        this.data = map;
        return this;
    }

    public R data(Map<String, Object> map) {
        this.data = map;
        return this;
    }


    /**
     * 设置
     * @param code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 设置
     * @param success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * 设置
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 设置
     * @param data
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
