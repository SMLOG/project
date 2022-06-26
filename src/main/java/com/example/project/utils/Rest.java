/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.utils;

import lombok.Data;

import java.io.Serializable;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
public class Rest implements Serializable {
    private Integer code;

    private String msg;

    private Object data;

    public Rest(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Rest(Integer code, String msg) {
        this(code, msg, null);
    }

    public Rest(ReturnCodeEnum returnCode, Object data) {
        this(returnCode.getCode(), returnCode.getMsg(), data);
    }

    public Rest(ReturnCodeEnum returnCode) {
        this(returnCode, null);
    }

    public Rest(ReturnCodeEnum returnCode, String msg, Object data) {
        this(returnCode.getCode(), msg, data);
    }

    public static Rest success() {
        return success(null);
    }

    /**
     * 返回成功
     *
     * @param data
     * @return
     */
    public static Rest success(Object data) {
        return new Rest(ReturnCodeEnum.SUCCESS, data);
    }

    public static Rest success(String msg, Object data) {
        return new Rest(ReturnCodeEnum.SUCCESS, msg, data);
    }

    public static Rest fail(Integer code, String msg, Object data) {
        return new Rest(code, msg, data);
    }

    public static Rest fail(Integer code, String msg) {
        return new Rest(code, msg);
    }

    public static Rest fail(ReturnCodeEnum returnCode) {
        return fail(returnCode.getCode(), returnCode.getMsg(), null);
    }

    /**
     * 返回错误
     *
     * @param returnCode
     * @param msg
     * @return
     */

    public static Rest fail(ReturnCodeEnum returnCode, String msg) {
        String errorMsg = String.format(  "%s", StringUtils.isEmpty(msg)?returnCode.getMsg() :msg);
        return fail(returnCode.getCode(), errorMsg, null);
    }

    @JsonIgnore
	public boolean isOK() {
		return this.getCode().intValue() == ReturnCodeEnum.SUCCESS.getCode().intValue();
	}
}
