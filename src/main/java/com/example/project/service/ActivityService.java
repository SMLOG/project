/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.project.dao.ActivityDao;
import com.example.project.dao.ActivityOptUserDao;
import com.example.project.dao.ActivityOptUserLogDao;
import com.example.project.dao.HouseDao;
import com.example.project.dao.PostDao;
import com.example.project.dao.UserDao;
import com.example.project.dao.UserHouseDao;
import com.example.project.model.Activity;
import com.example.project.model.ActivityCandidate;
import com.example.project.model.ActivityVoteLog;
import com.example.project.model.House;
import com.example.project.model.User;
import com.example.project.model.UserHouse;
import com.example.project.utils.Rest;
import com.example.project.utils.RestException;
import com.example.project.utils.SecurityUtils;
import com.example.project.vobean.UserVo;

@Service
public class ActivityService {

	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private ActivityOptUserDao candidateDao;

	@Autowired
	private ActivityOptUserLogDao voteLogDao;

	@Autowired
	private UserHouseDao userHouseDao;
	@Autowired
	private UserDao userDao;

	@Autowired
	private PostDao postDao;

	@Autowired
	private HouseDao houseDao;

	public Rest publishActivity(Activity activity) {

		if(
				 activity.getCanEndDate() ==null 
				|| activity.getVoteStartDate() == null
				|| activity.getVoteEndDate() == null
				) {
			return Rest.fail("日期不能为空！");
		}
		
		if(! ( activity.getVoteStartDate().compareTo(activity.getCanEndDate())>=0
				&& activity.getVoteEndDate().compareTo(activity.getVoteStartDate())>=0)
				
				) {
			return Rest.fail("日期顺序不正确！");
		}
		
		if(activity.getAtleaseCans()==null ||activity.getAtleaseCans().intValue()==0) {
			return Rest.fail("候选人至少不能为0");
		}
		
		if(activity.getUserType()==null ) {
			return Rest.fail("参与主体需要选择");
		}
		if(activity.getSubject()==null || activity.getSubject().trim().length()<10) {
			return Rest.fail("主题不能少于10个字符！");
		}
		activityDao.save(activity);

		postDao.createPost(activity.getActivityId(), activity.getSubject(), activity.getDescr(), "activity");

		return Rest.success(activity);
	}

	public Rest registCandidate(Integer activityId, ActivityCandidate info) throws Exception {

		Integer userId = SecurityUtils.getCurrentUserId();

		ActivityCandidate optUser = new ActivityCandidate();
		optUser.setUserId(userId);
		optUser.setActivityId(activityId);
		if (candidateDao.findOne(Example.of(optUser)).isPresent()) {

		} else {
			
			Activity activity = activityDao.findById(activityId).get();

			userHouseDao.getUserTypeTicks(activity.getUserType(),userId,"报名");

			
			optUser.setDescr(info.getDescr());
			optUser.setTel(info.getTel());
			optUser.setNickName(info.getNickName());
			optUser.setTikets(0);
			optUser.setPic(info.getPic());
			
			optUser.setHouseIds(userHouseDao.getUserHouseIds(userId,activity.getUserType()));

			candidateDao.save(optUser);

		}

		return Rest.success();
	}
	public Rest unRegistCandidate(Integer activityId) {
		Integer userId = SecurityUtils.getCurrentUserId();

		ActivityCandidate optUser = new ActivityCandidate();
		optUser.setUserId(userId);
		optUser.setActivityId(activityId);
		if (candidateDao.findOne(Example.of(optUser)).isPresent()) {
			
			candidateDao.delete(candidateDao.findOne(Example.of(optUser)).get());
		}
		
		return Rest.success();
		
	}
	
	public Rest getActivityDetail(Integer activityId) {

		Activity a = activityDao.findById(activityId).get();
		ActivityCandidate opt = new ActivityCandidate();
		opt.setActivityId(a.getActivityId());
		List<ActivityCandidate> canList = candidateDao.findAll(Example.of(opt));
		ActivityVoteLog activityOptUserLogOpt = new ActivityVoteLog();
		activityOptUserLogOpt.setActivityId(a.getActivityId());
		Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
		List<ActivityVoteLog> voteLogList = voteLogDao.findAll(Example.of(activityOptUserLogOpt),sort);

		HashMap<String, Object> ret = new HashMap<String, Object>();
		
		LocalDate now = LocalDate.now();
		int status = -1;
		if(now.compareTo(a.getCanEndDate())<=0) {
			status = 0;
		}
		if(now.compareTo(a.getVoteStartDate())>=0 && now.compareTo(a.getVoteEndDate())<=0 && canList.size()>= a.getAtleaseCans()) {
			status = 1;
		}else if(now.compareTo(a.getVoteEndDate())>0) {
			status=2;
		}
		ret.put("status",status);
		ret.put("activity", a);
		ret.put("candidates", canList);
		ret.put("logs", voteLogList);
		ret.put("userId", SecurityUtils.getCurrentUserId());

		List<Integer> canUserList = canList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
		List<Integer> voteLogUserList = voteLogList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
		canUserList.addAll(voteLogUserList);
		HashMap<Integer, UserVo> userIdMap = new HashMap<Integer, UserVo>();
		for (User user : userDao.findAllById(canUserList)) {

			UserVo userVo = new UserVo();
			userVo.setId(user.getUserId());
			userVo.setNickName(user.getNickName());
			userVo.setUserName(user.getUserName());
			userVo.setMask(true);

			userIdMap.put(user.getUserId(), userVo);

		}

		ret.put("userIdMap", userIdMap);


		Map<Integer, House> houseIdMap = new HashMap<Integer, House>();

		for (House h : houseDao.findAll()) {
			houseIdMap.put(h.getHouseId(), h);
		}
		ret.put("houseIdMap", houseIdMap);

		return Rest.success(ret);
	}

	public Rest voteCandidate(Integer activityId, List<ActivityCandidate> cans) throws Exception {
		Integer userId = SecurityUtils.getCurrentUserId();

		ActivityVoteLog optUserLog = new ActivityVoteLog();
		optUserLog.setUserId(userId);
		optUserLog.setActivityId(activityId);

		if (voteLogDao.findAll(Example.of(optUserLog)).size() > 0) {
			
			return Rest.fail("你已经投票了！");

		} else {

			optUserLog = new ActivityVoteLog();
			optUserLog.setUserId(userId);
			optUserLog.setActivityId(activityId);

			Activity activity = activityDao.findById(activityId).get();
			
			optUserLog.setHouseIds(userHouseDao.getUserHouseIds(userId,activity.getUserType()));

			
			int ticks= userHouseDao.getUserTypeTicks(activity.getUserType(),userId,"投票");


			for (ActivityCandidate can : cans) {
				ActivityCandidate entity = candidateDao.findById(can.getCanId()).get();
				if (entity.getActivityId().intValue() != activityId.intValue()) {

					return Rest.fail("非法数据！");
				}

				entity.setTikets(entity.getTikets() + ticks);
				candidateDao.save(entity);
			}

			String canIds = cans.stream().map(e -> e.getCanId().toString()).collect(Collectors.joining(","));

			optUserLog.setCanIds(canIds);
			voteLogDao.save(optUserLog);

		}

		return Rest.success();
	}





}
