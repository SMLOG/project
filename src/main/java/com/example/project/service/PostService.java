/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.project.dao.PostDao;

@Service
public class PostService {



	@Autowired
	private PostDao postDao;
	
	public Object getPosts(int page) {

		Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
		PageRequest pr = PageRequest.of(page, 10, sort);
		return postDao.findAll(pr);
	}



}
