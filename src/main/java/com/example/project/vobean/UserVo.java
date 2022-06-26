/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.vobean;

import lombok.Data;

@Data
public class UserVo {
	private Integer id;
	private String userName;
	private String nickName;
	private String oldPwd;
	private String pwd;
	private String pwd2;
	private String captcha;
	private String email;

}
