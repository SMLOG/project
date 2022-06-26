/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.project.dao.UserDao;
import com.example.project.model.User;
import com.example.project.utils.Rest;
import com.example.project.utils.ResultStatus;
import com.example.project.utils.ReturnCodeEnum;
import com.example.project.vobean.UserVo;


@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;

	
	public Rest createUser(UserVo userVo) {
		
		if(userVo!=null) {
			
			User user = new User();
			user.setUserName(userVo.getUserName());
			user.setEmail(userVo.getUserName());
			user.setNickName(userVo.getNickName());
			
			Optional<User> existsUser = userDao.findOne(Example.of(user));
			
			
			if(!existsUser.isPresent()) {

				user.setPwd(userVo.getPwd());
				userDao.save(user);
				userVo.setId(user.getUserId());
				
				//userVo.setPwd(null);
				//userVo.setPwd2(null);
				
				return Rest.success(userVo);
			}

		}
		return  Rest.fail(ReturnCodeEnum.USER_EXISTS);

	}

	public UserVo login(UserVo userVo) {
		User user = new User();
		user.setUserName(userVo.getUserName());
		Optional<User> existsUser = userDao.findOne(Example.of(user));
		
		if(existsUser.isPresent()) {
			
			if(existsUser.get().getPwd().equals(userVo.getPwd())) {
				
				UserVo loginUser = new UserVo();
				
				BeanUtils.copyProperties(existsUser.get(), loginUser);
				loginUser.setPwd(null);
				loginUser.setPwd2(null);
				loginUser.setId(existsUser.get().getUserId());

				return loginUser;
			}
		}
		return null;
	}

	public List<UserVo> getUsers() {
		return userDao.findAll().stream().map( e->{
			UserVo vo = new UserVo();
			vo.setId(e.getUserId());
			vo.setUserName(e.getUserName());
			return vo;
		}).collect(Collectors.toList());
	}


}
