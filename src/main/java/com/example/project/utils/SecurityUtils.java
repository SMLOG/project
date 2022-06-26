/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.utils;

import com.example.project.vobean.UserVo;

import cn.dev33.satoken.stp.StpUtil;

public class SecurityUtils {

	public static Integer getCurrentUserId() {
		if(StpUtil.isLogin()) {
			UserVo userVo = (UserVo) StpUtil.getSession().get("user");
			
			return userVo==null?0:userVo.getId();
		}
		return 0;

	}

}
