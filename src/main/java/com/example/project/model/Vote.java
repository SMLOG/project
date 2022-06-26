/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Vote extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer voteId;
	private String subject;
	private String descr;
	private Integer minSelect;
	private Integer maxSelect;

	private LocalDate startDate;
	private LocalDate endDate;
	private Integer voterRole;
	private boolean canHideName;
	
	private Integer joinUserCount;

}
