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
public class Cost extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer costId;
	private String code;
	private String buildNo;
	private String type;
	private Double val1;
	private Double val2;
	private Double val3;
	private LocalDate cutDate;
	private Integer period;
	private Double total;
	private Double pval1;
	private LocalDate pcutDate;

	
}
