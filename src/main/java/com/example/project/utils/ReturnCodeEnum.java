/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.utils;

public enum ReturnCodeEnum {
    SUCCESS(0, "成功"),
    SYSTEM_ERROR(10001, "系统错误"),
    UNKNOWN_ERROR(10002, "未知错误"),

    USER_LOGIN_ERROR(20001, "登录异常"),
    USER_EXISTS(20002, "用户名已存在"),
    USER_NAME_OR_PWD_ERROR(20001, "用户名或者密码错误");

    private Integer code;
    private String msg;

    ReturnCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

	public static ReturnCodeEnum getReturnCode(Integer code) {
        for (ReturnCodeEnum rc : values()) {
            if (rc.code == code) {
                return rc;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
