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
import com.example.project.model.House;
import com.example.project.model.UserHouse;
import com.example.project.model.Vote;
import com.example.project.model.VoteOpt;
import com.example.project.service.HouseService;
import com.example.project.utils.Rest;
import com.example.project.utils.ReturnCodeEnum;
import com.example.project.utils.SecurityUtils;
import com.example.project.vobean.UserHouseVo;

@RestController
@RequestMapping("/api")
public class HouseController {

	@Autowired
	private HouseService houseService;

	@RequestMapping("/initHouses")
	public Rest initHouses(@RequestBody List<House> hses) {

		return Rest.success(houseService.initHouses(hses));

	}
	@RequestMapping("/houses")
	public Rest initHouses() {

		return Rest.success(houseService.getHouseList());

	}
	
	@RequestMapping("/bindHouses")
	public Rest addMyHouse(@RequestBody  List<House> houses) {

		return Rest.success(houseService.userBindHouses(houses));

	}
	@RequestMapping("/myHouses")
	public Rest myHouses() {

		return Rest.success(houseService.myHouses());

	}
	@RequestMapping("/unBindHouses")
	public Rest unBindHouses() {

		return Rest.success(houseService.unBindUserHouses());

	}
	
	
	@RequestMapping("/reqAuthMyHouse.json")
	public Rest reqAuthMyHouse(@RequestBody  UserHouseVo vo) {

		if(SecurityUtils.getCurrentUserId().intValue() != vo.getUserId().intValue()) {
			return Rest.fail(ReturnCodeEnum.UNKNOWN_ERROR);
		}
		return Rest.success(houseService.reqAuthMyHouse(vo));

	}
	

	
	@RequestMapping("/authReq.json")
	public Rest getAuthRequests() {
		
		return Rest.success(houseService.getAuthRequests());

	}
	
	
	@RequestMapping("/approveAuthReq.json")
	public Rest approveAuthReq(@RequestBody  UserHouseVo vo) {
		
		return houseService.approveAuthReq(vo);

	}

}
