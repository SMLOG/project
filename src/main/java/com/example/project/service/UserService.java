/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project.dao.SysParamDao;
import com.example.project.dao.UserDao;
import com.example.project.model.SysParam;
import com.example.project.model.User;
import com.example.project.utils.Rest;
import com.example.project.utils.ResultStatus;
import com.example.project.utils.ReturnCodeEnum;
import com.example.project.utils.SecurityUtils;
import com.example.project.vobean.UserVo;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private SysParamDao sysParamDao;

	@Resource
	private PasswordEncoder passwordEncoder;  //注入bcryct加密
	
	public Rest registerUser(UserVo userVo) {

		if (userVo != null) {

			SysParam param = new SysParam();
			param.setName("inviteCode");

			Optional<SysParam> paramOpt = sysParamDao.findOne(Example.of(param));
			if (!paramOpt.isPresent() || paramOpt.get().getValue().equals(userVo.getInviteCode())) {

				User user = new User();
				user.setUserName(userVo.getUserName());
				// user.setEmail(userVo.getUserName());
				

				Optional<User> existsUser = userDao.findOne(Example.of(user));

				if (!existsUser.isPresent() || existsUser.get().getStatus().intValue() == User.PENDING.intValue()) {

					user.setNickName(userVo.getNickName());
					user.setStatus(User.PENDING);
					user.setPwd(passwordEncoder.encode(userVo.getPwd()));
					userDao.save(user);
					userVo.setId(user.getUserId());

					// userVo.setPwd(null);
					// userVo.setPwd2(null);

					return Rest.success(userVo);
				}else {
					return Rest.fail("该手机号码已经注册过了,请登录。");

				}

			}

		}
		return Rest.fail(ReturnCodeEnum.USER_EXISTS);

	}

	public UserVo login(UserVo userVo) {
		User user = new User();
		user.setUserName(userVo.getUserName());
		Optional<User> userOpt = userDao.findOne(Example.of(user));

		if (userOpt.isPresent()) {

			user = userOpt.get();
			if (passwordEncoder.matches(userVo.getPwd(),user.getPwd())) {

				user.setLastLoginTime(user.getLoginTime());
				user.setLoginTime(LocalDateTime.now());
				UserVo loginUser = SecurityUtils.userToVo(userOpt.get());
				userDao.save(user);
				
				return loginUser;
			}
		}
		return null;
	}

	public List<UserVo> getUsers() {
		return userDao.findAll().stream().map(e -> {
			UserVo vo = new UserVo();
			vo.setId(e.getUserId());
			vo.setUserName(e.getUserName());
			return vo;
		}).collect(Collectors.toList());
	}

	public Rest changePass(@Valid UserVo userVo) {

		if (!userVo.getPwd().equals(userVo.getPwd2())) {
			return Rest.fail("两次密码不一致");
		}
		Optional<User> userOpt = userDao.findById(SecurityUtils.getCurrentUserId());
		if (userOpt.isPresent()) {

			User user = userOpt.get();
			if (user.getPwd().equals(userVo.getOldPwd())) {

				user.setPwd(userVo.getPwd());
				userDao.save(user);

				return Rest.success();
			} else
				return Rest.fail("旧密码错误");

		}

		return Rest.fail();
	}

}
