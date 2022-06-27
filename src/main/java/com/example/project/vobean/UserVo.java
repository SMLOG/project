/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.vobean;

import java.time.LocalDateTime;

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
	private String inviteCode;
	private Integer authNum;
	private Integer status;
	private LocalDateTime createDate;
	private LocalDateTime lastLoginTime;
	private boolean mask;
	
	public String getUserName() {
		
		return this.userName!=null&&mask && this.userName.length()>=4  ? this.userName.substring(this.userName.length()-4):this.userName;
	}

}
