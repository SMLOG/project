/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.apiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.service.UserService;
import com.example.project.utils.Rest;

@RestController
@RequestMapping("/api/system/admin") 
public class AdminController {
	@Autowired
	private UserService userService;
	@RequestMapping("users")
	public Rest userList() {
		
	 return Rest.success(userService.getUsers());
	}

}
