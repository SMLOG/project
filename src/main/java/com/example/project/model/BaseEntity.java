/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.model;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@EntityListeners(AuditingEntityListener.class)
@Data
@MappedSuperclass
public abstract class BaseEntity {

	@CreatedDate
	private LocalDateTime createDate;
	@CreatedBy
	private Integer createBy;
	@LastModifiedBy
	private Integer updateBy;
	@LastModifiedDate
	private LocalDateTime udateDate;
	
}
