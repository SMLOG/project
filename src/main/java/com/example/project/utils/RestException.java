/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.utils;

import lombok.Data;

@Data
public class RestException extends Exception {
	private  Rest rest;

	public RestException(Rest rest) {
		super();
		this.rest = rest;
	}
	

}
