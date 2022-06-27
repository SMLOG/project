/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Entity
@Data
public class SysParam extends BaseEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer parmaId;
	private String name;
	private String value;
	private char status;

}
