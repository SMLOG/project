/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.apiController;

import java.util.List;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.project.model.Cost;
import com.example.project.model.House;
import com.example.project.model.User;
import com.example.project.model.UserHouse;
import com.example.project.model.Vote;
import com.example.project.model.VoteOpt;
import com.example.project.service.CostSerivce;
import com.example.project.service.HouseService;
import com.example.project.utils.Rest;
import com.example.project.utils.ReturnCodeEnum;
import com.example.project.utils.SecurityUtils;
import com.example.project.vobean.UserHouseVo;
import com.example.project.vobean.UserVo;

@RestController
@RequestMapping("/api")
public class CostController {

	@Autowired
	private CostSerivce costService;

	@RequestMapping(path="/cost/{period}",method=RequestMethod.POST)
	public Rest saveCost(@RequestBody List<Cost> costList,@PathVariable Integer period) {

		return Rest.success(costService.saveCost(period,costList));

	}
	@RequestMapping(path="/cost/{period}",method=RequestMethod.GET)
	public Rest getCostList(@PathVariable Integer period) {

		return costService.getPeriodCostList(period);

	}

}
