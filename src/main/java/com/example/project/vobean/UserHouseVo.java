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
public class UserHouseVo {
	
	private Integer id;
	private Integer userId;
	private Integer authStatu;
	private String authCode;
	private LocalDateTime reqAuthDateTime;
	
	private Integer houseId;
	private String buildNo;
	private String floorNo;
	private String roomNo;
	private Integer m2;
	private Integer ownerId;
	private String appvUserIds;

}
