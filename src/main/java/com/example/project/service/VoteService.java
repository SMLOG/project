/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.service;

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
import com.example.project.dao.UserHouseDao;
import com.example.project.dao.VoteDao;
import com.example.project.dao.VoteLogDao;
import com.example.project.dao.VoteOptDao;
import com.example.project.model.House;
import com.example.project.model.UserHouse;
import com.example.project.model.Vote;
import com.example.project.model.VoteLog;
import com.example.project.model.VoteOpt;
import com.example.project.utils.SecurityUtils;
import com.example.project.vobean.UserHouseVo;

@Service
public class VoteService {



	@Autowired
	private VoteDao voteDao;
	@Autowired
	private VoteOptDao voteOptDao;

	@Autowired
	private VoteLogDao voteLogDao;





	public Object newVote(Vote vote, List<VoteOpt> voteOpts) {
		vote.setVoteId(null);

		vote.setJoinUserCount(0);

		voteDao.save(vote);

		voteOpts.forEach(o -> {

			o.setVoteId(vote.getVoteId());
			o.setOptId(null);
			o.setCurCount(0);

		});

		voteOptDao.saveAll(voteOpts);

		return null;
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
			ret.put("voteLogs", logs);

		}
		return ret;
	}

	public Object vote(Integer voteId, List<VoteOpt> opts) {

		VoteLog log = new VoteLog();
		log.setVoteUserId(SecurityUtils.getCurrentUserId());
		log.setVoteId(voteId);

		List<VoteLog> logs = voteLogDao.findAll(Example.of(log));
		if (logs.size() == 0 && opts.size() > 0
				&& !opts.stream().anyMatch(e -> e.getVoteId().intValue() != voteId.intValue())) {

			Vote vote = voteDao.findById(voteId).get();
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

					voteLogDao.save(log);

					voteOpt.setCurCount(voteOpt.getCurCount() + 1);

					voteOptDao.save(voteOpt);

				}

			}

		}

		return null;
	}



}
