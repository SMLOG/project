/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.VoteLog;

@Repository
public interface VoteLogDao  extends JpaRepository<VoteLog,Integer>{

}
