/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.UserHouse;
import com.example.project.model.UserHouseAuthLog;

@Repository
public interface UserHouseAuthLogDao  extends JpaRepository<UserHouseAuthLog,Integer>{

}
