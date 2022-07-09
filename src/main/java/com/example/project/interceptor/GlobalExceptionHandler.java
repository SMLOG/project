/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project.utils.Rest;
import com.example.project.utils.RestException;
import com.example.project.utils.ReturnCodeEnum;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Rest exceptionHandler(HttpServletRequest request, Exception e) {
    	if(e instanceof RestException) {
    		return ((RestException)(e)).getRest();
    	}
    	
    	return Rest.fail(ReturnCodeEnum.SYSTEM_ERROR, e.getCause()!=null ?e.getCause().getMessage():null);
    }

}
