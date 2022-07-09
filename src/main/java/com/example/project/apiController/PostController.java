/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.apiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.service.PostService;
import com.example.project.utils.Rest;

@RestController
public class PostController {

	@Autowired
	private PostService postService;


	@RequestMapping("/api/posts")
	public Rest getVotes(@RequestParam(defaultValue = "0") Integer page) {
		
		return Rest.success(postService.getPosts(page));

	}
	

}
