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
import com.example.project.model.ActivityCandidate;
import com.example.project.model.ActivityVoteLog;

@Repository
public interface ActivityOptUserLogDao extends JpaRepository<ActivityVoteLog, Integer> {

}
