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

import lombok.Data;

@Entity
@Data
public class UserHouse extends BaseEntity {
	public static final Integer NO_AUTH = 0;
	public static final Integer AUTH_1 = 1;//自验证房产
	public static final Integer AUTH_2 = 2;//已验证房产
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer houseId;
	private Integer userId;
	private Integer authStatu;
	private String authCode;
	private LocalDateTime reqAuthDateTime;
	private String appvUserIds;
	

}
