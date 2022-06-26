/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.utils;


public class ResultStatus {


	public ResultStatus(int i, String string,Object data) {
		this.statusCode =i ;
		this.desc = string;
		this.data = data;
	}
	 
	private int statusCode;
	private String desc;
	private Object data;
	
	public boolean isOK() {
	return	statusCode == 0;
	}
	
	public Object getData() {
		return data;
	}

}
