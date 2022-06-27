/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.User;


@Repository
public interface UserDao extends JpaRepository<User,Integer>{

	Page<User> findByStatusLessThan(Integer housealid1, PageRequest pr);

	List<User> findByStatusLessThan(Integer housealid1);
	

}
