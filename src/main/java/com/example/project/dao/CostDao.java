/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.dao;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.Cost;
import com.example.project.model.House;

@Repository
public interface CostDao  extends JpaRepository<Cost,Integer>{



	Cost findFirstByOrderByPeriodDesc();


}
