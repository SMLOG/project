/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;

import com.example.project.model.User;
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
	
	public static Map<String, Object> bindUserToSession(UserVo user) {
		
        StpUtil.login(user.getUserName(),true);
        Map<String, Object> result = new HashMap<>();
        result.put("token", StpUtil.getTokenInfo());
        result.put("user", user);
        StpUtil.getSession().set("user", user);
        return result;
	}

	public static UserVo userToVo(User user) {
		UserVo loginUser = new UserVo();

		BeanUtils.copyProperties(user, loginUser);
		loginUser.setPwd(null);
		loginUser.setPwd2(null);
		loginUser.setId(user.getUserId());
		return loginUser;
	}
	
}
