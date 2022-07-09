/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.project.dao.HouseDao;
import com.example.project.dao.PostDao;
import com.example.project.dao.UserHouseDao;
import com.example.project.dao.VoteDao;
import com.example.project.dao.VoteLogDao;
import com.example.project.dao.VoteOptDao;
import com.example.project.model.Activity;
import com.example.project.model.House;
import com.example.project.model.User;
import com.example.project.model.UserHouse;
import com.example.project.model.Vote;
import com.example.project.model.VoteLog;
import com.example.project.model.VoteOpt;
import com.example.project.utils.Rest;
import com.example.project.utils.SecurityUtils;
import com.example.project.vobean.UserHouseVo;
import com.example.project.vobean.UserVo;

@Service
public class VoteService {



	@Autowired
	private VoteDao voteDao;
	@Autowired
	private VoteOptDao voteOptDao;

	@Autowired
	private VoteLogDao voteLogDao;


	@Autowired
	private PostDao postDao;

	
	@Autowired
	private UserHouseDao userHouseDao;
	
	@Autowired
	private HouseDao houseDao;
	

	public Rest newVote(Vote vote, List<VoteOpt> voteOpts) {
		
		if(vote.getSubject()==null || vote.getSubject().trim().length()<10 || vote.getSubject().trim().length()>200) {
			return Rest.fail("主题不能少于10个字符和多于200个字符！");
		}
		
		if(vote.getDescr()==null || vote.getDescr().trim().length()<10 || vote.getDescr().trim().length()>200) {
			return Rest.fail("简介内容不能少于10个字符和多于200个字符！");
		}
		
		if(vote.getUserType()==null) {
			return Rest.fail("请选择参与主体！");
		}
		

		if(LocalDate.now().compareTo(vote.getEndDate())>0) {
			return Rest.fail("截止日期不能早于当前日期！");
		}
		
		if(vote.getMinSelect()==null || vote.getMaxSelect() ==null) {
			return Rest.fail("最少选择和最多选择不能为空！");
		}
		if(vote.getMinSelect().intValue()>vote.getMaxSelect().intValue()) {
			return Rest.fail("最少选择不能大于最多选择！");

		}
		
		if(voteOpts==null || voteOpts.size() <2 ) {
			return Rest.fail("选项不能少于2项！");
		}
		
		for(int i=0;i<voteOpts.size();i++) {
			if(i==0) {
				VoteOpt opt = voteOpts.get(i);
				if(opt.getName()==null||opt.getName().trim().length()<1 || opt.getName().length()>200)
					return Rest.fail("选项内容少于1和多于200个字符！");	
			}

			for(int j=i+1;j<voteOpts.size();j++) {
				VoteOpt opt = voteOpts.get(j);
				if(opt.getName()==null||opt.getName().trim().length()<1 || opt.getName().length()>200)
					return Rest.fail("选项内容少于1和多于200个字符！");
				
				if(opt.getName().equals(voteOpts.get(i).getName())) {
					return Rest.fail("选项内容不能有相同项！");

				}
			}
		}
		
		
		vote.setVoteId(null);

		vote.setJoinUserCount(0);

		voteDao.save(vote);

		voteOpts.forEach(o -> {

			o.setVoteId(vote.getVoteId());
			o.setOptId(null);
			o.setCurCount(0);

		});

		voteOptDao.saveAll(voteOpts);
		
		postDao.createPost(vote.getVoteId(), vote.getSubject(),vote.getDescr(),"vote");

		return Rest.success();
	}

	public Object getVotes(int page) {

		Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
		PageRequest pr = PageRequest.of(page, 10, sort);
		return voteDao.findAll(pr);
	}

	public Object getVoteDetail(Integer voteId) {
		Optional<Vote> vote = voteDao.findById(voteId);
		Map<String, Object> ret = new HashMap<String, Object>();
		if (vote.isPresent()) {
			ret.put("vote", vote);
			VoteOpt exopt = new VoteOpt();
			exopt.setVoteId(voteId);
			List<VoteOpt> opts = voteOptDao.findAll(Example.of(exopt));
			ret.put("voteOpts", opts);

			VoteLog log = new VoteLog();
			log.setVoteId(voteId);

			List<VoteLog> logs = voteLogDao.findAll(Example.of(log));
			ret.put("logs", logs);

			log.setVoteUserId(SecurityUtils.getCurrentUserId());

			logs = voteLogDao.findAll(Example.of(log));
			ret.put("myLogs", logs);
			
			ret.put("status", vote.get().getEndDate().compareTo(LocalDate.now())<0?1:0);	
	
		
			Map<Integer, House> houseIdMap = new HashMap<Integer, House>();

			for (House h : houseDao.findAll()) {
				houseIdMap.put(h.getHouseId(), h);
			}
			ret.put("houseIdMap", houseIdMap);

		}
		return ret;
	}

	public Rest vote(Integer voteId, List<VoteOpt> opts) throws Exception {

		VoteLog log = new VoteLog();
		log.setVoteUserId(SecurityUtils.getCurrentUserId());
		log.setVoteId(voteId);

		UserHouse uh = new UserHouse();
		uh.setUserId(SecurityUtils.getCurrentUserId());

		Vote vote = voteDao.findById(voteId).get();
		
		int ticks = userHouseDao.getUserTypeTicks(vote.getUserType(),SecurityUtils.getCurrentUserId(),"投票");

		
		List<VoteLog> logs = voteLogDao.findAll(Example.of(log));
		if (logs.size() == 0 && opts.size() > 0
				&& !opts.stream().anyMatch(e -> e.getVoteId().intValue() != voteId.intValue())) {

			if (opts.size() >= vote.getMinSelect() && opts.size() <= vote.getMaxSelect()) {

				vote.setJoinUserCount(vote.getJoinUserCount() + 1);
				voteDao.save(vote);
				for (int i = 0; i < opts.size(); i++) {
					VoteOpt voteOpt = voteOptDao.findById(opts.get(i).getOptId()).get();
					log = new VoteLog();
					log.setVoteUserId(SecurityUtils.getCurrentUserId());
					log.setVoteId(voteId);
					log.setVoteOpdId(opts.get(i).getOptId());
					log.setVoteTime(LocalTime.now());
					log.setHouseIds(userHouseDao.getUserHouseIds(SecurityUtils.getCurrentUserId(), vote.getUserType()));
					voteLogDao.save(log);

					voteOpt.setCurCount(voteOpt.getCurCount() + ticks);

					voteOptDao.save(voteOpt);

				}

			}

		}

		return Rest.success();
	}



}
