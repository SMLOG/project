/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Activity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer activityId;
	private String subject;
	private String descr;

	private LocalDate canStartDate;
	private LocalDate canEndDate;
	
	private LocalDate voteStartDate;
	private LocalDate voteEndDate;
	
	private Integer joinUserCount;
	private Integer atleaseCans;
	private Integer userType;

}
