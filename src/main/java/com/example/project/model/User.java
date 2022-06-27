/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Entity
@Data
public class User extends BaseEntity {
	public static final Integer PENDING = 0;
	public static final Integer TELVALID = 1;
	public static final Integer HOUSEALID1 = 2;
	public static final Integer HOUSEALID2 = 4;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userId;
	private String userName;
	private String pwd;
	private String email;
	private String nickName;
	private String tel;
	private Integer authNum;
	private Integer status;
	private LocalDateTime lastLoginTime;
	private LocalDateTime loginTime;

}
