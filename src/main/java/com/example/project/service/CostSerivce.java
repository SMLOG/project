/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.project.dao.CostDao;
import com.example.project.dao.CostPeriodDao;
import com.example.project.dao.HouseDao;
import com.example.project.model.Cost;
import com.example.project.model.CostPeriod;
import com.example.project.utils.Rest;

@Service
public class CostSerivce {

	@Autowired
	private CostDao costDao;

	@Autowired
	private HouseDao houseDao;

	@Autowired
	private CostPeriodDao periodDao;
	
	public Object saveCost(Integer period, List<Cost> costList) {

		
		for(Cost cost:costList) {
			
			cost.setPeriod(period);
			
			Cost ex = new Cost();
			ex.setCode(cost.getCode());
			ex.setBuildNo(cost.getBuildNo());
			ex.setPeriod(period);
			
			Optional<Cost> old = costDao.findOne(Example.of(ex));
			if(old.isPresent()) {
				cost.setCostId(old.get().getCostId());
			}
		}
		costDao.saveAll(costList);
		
		
		return null;
	}

	public Rest getPeriodCostList(Integer period) {
		
		
		Cost ex = new Cost();
		
		if(period==null || period.intValue()==0) {
			Cost last = costDao.findFirstByOrderByPeriodDesc();
			if(last!=null) ex.setPeriod(last.getPeriod());

		}else
		ex.setPeriod(period);
		
		
		List<Cost> costs = costDao.findAll(Example.of(ex));
		ex.setPeriod(period%10-1>0?period-1:(period/100-1)*100);
		
		Sort sort = Sort.by(Sort.Direction.DESC, "buildNo","type");
		
		
		List<Cost> prevCosts = costDao.findAll(Example.of(ex),sort);
		
		if(prevCosts!=null && prevCosts.size()>0) {
			HashMap<String,Cost> map = new HashMap<String,Cost>();
			prevCosts.forEach(e->{
				map.put(e.getCode()+e.getBuildNo(), e);
			});
			
			HashMap<String,Cost> curMap = new HashMap<String,Cost>();
			
			costs.forEach(e->{
				
				Cost prevCost = map.get(e.getCode()+e.getBuildNo());
				if(prevCost!=null) {
					e.setPval1(prevCost.getVal1());
					e.setPcutDate(prevCost.getCutDate());
				}
				curMap.put(e.getCode()+e.getBuildNo(), e);
				
			});
			
			prevCosts.forEach(e->{
				if(curMap.get(e.getCode()+e.getBuildNo())==null) {
					Cost cost = new Cost();
					cost.setPval1(e.getVal1());
					cost.setPcutDate(e.getPcutDate());
					cost.setPeriod(period);
					cost.setCode(e.getCode());
					cost.setType(e.getType());
					
					costs.add(cost);
				}
			});
			
		}


		
		return Rest.success(costs);
	}

	



}
