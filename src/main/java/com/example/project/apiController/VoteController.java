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
import com.example.project.service.VoteService;
import com.example.project.utils.Rest;
import com.example.project.utils.ReturnCodeEnum;
import com.example.project.utils.SecurityUtils;
import com.example.project.vobean.UserHouseVo;

@RestController
public class VoteController {

	@Autowired
	private VoteService voteService;


	
	
	@RequestMapping("/api/vote")
	public Rest publishVote(@RequestBody  String json) {
		
		JSONObject jsonObj = JSON.parseObject(json);
		Vote vote = jsonObj.getObject("vote",Vote.class);
		 JSONArray array = jsonObj.getJSONArray("voteOpts");
		List<VoteOpt> voteOpts = array.toJavaList(VoteOpt.class);

		return voteService.newVote(vote,voteOpts);

	}
	
	@RequestMapping("/api/votes")
	public Rest getVotes(@RequestParam(defaultValue = "0") Integer page) {
		


		return Rest.success(voteService.getVotes(page));

	}
	
	
	
	
	@RequestMapping(path="/api/vote/{voteId}.json",method=RequestMethod.GET)
	public Rest voteDetail(@PathVariable Integer voteId) {

		return Rest.success(voteService.getVoteDetail(voteId));

	}
	
	@RequestMapping(path="/api/vote/{voteId}.json",method=RequestMethod.POST)
	public Rest vote(@PathVariable Integer voteId,@RequestBody List<VoteOpt> opts) throws Exception {

		return voteService.vote(voteId,opts);

	}
}
