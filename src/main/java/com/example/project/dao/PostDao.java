/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.Activity;
import com.example.project.model.Post;

@Repository
public interface PostDao extends JpaRepository<Post, Integer> {

	default void createPost(Integer refId,String subject, String descr, String type) {
		
		Post post = new Post();
		post.setSubject(subject);
		post.setDescr(descr);
		post.setName(type);
		post.setRefId(refId);
		this.save(post);
		
	}

}
