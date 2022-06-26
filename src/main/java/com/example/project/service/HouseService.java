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
import com.example.project.dao.UserHouseAuthLogDao;
import com.example.project.dao.UserHouseDao;
import com.example.project.dao.VoteDao;
import com.example.project.dao.VoteLogDao;
import com.example.project.dao.VoteOptDao;
import com.example.project.model.House;
import com.example.project.model.UserHouse;
import com.example.project.model.UserHouseAuthLog;
import com.example.project.model.Vote;
import com.example.project.model.VoteLog;
import com.example.project.model.VoteOpt;
import com.example.project.utils.Rest;
import com.example.project.utils.ReturnCodeEnum;
import com.example.project.utils.SecurityUtils;
import com.example.project.vobean.UserHouseVo;

@Service
public class HouseService {

	@Autowired
	private HouseDao houseDao;

	@Autowired
	private UserHouseDao userHouseDao;


	@Autowired
	private UserHouseAuthLogDao userHouseAuthLogDao;


	public Object initHouses(List<House> list) {

		// houseDao.deleteAll();
		houseDao.saveAll(list);

		return null;
	}

	public List<House> getHouseList() {
		return houseDao.findAll();
	}

	public Object addMyHouse(House house) {
		Optional<House> entityHouse = houseDao.findById(house.getHouseId());

		if (entityHouse.isPresent()) {
			Integer userId = SecurityUtils.getCurrentUserId();
			UserHouse userHouse = new UserHouse();
			userHouse.setHouseId(house.getHouseId());
			userHouse.setUserId(userId);
			userHouse.setAuthStatu(0);
			List<UserHouse> exists = userHouseDao.findAll(Example.of(userHouse));
			if (exists.size() == 0) {
				userHouseDao.save(userHouse);
			}
		}

		return null;
	}

	public Object myHouses() {
		Integer userId = SecurityUtils.getCurrentUserId();
		UserHouse userHouse = new UserHouse();
		userHouse.setUserId(userId);
		List<UserHouse> exists = userHouseDao.findAll(Example.of(userHouse));

		List<UserHouseVo> retList = new ArrayList<UserHouseVo>();

		if (exists.size() > 0) {
			List<Integer> ids = exists.stream().map(e -> e.getHouseId()).collect(Collectors.toList());

			Stream<House> houseListStream = houseDao.findAllById(ids).stream();
			Map<Integer, House> houseMap = new HashMap<Integer, House>();

			houseListStream.forEach(e -> {
				houseMap.put(e.getHouseId(), e);

			});
			exists.forEach(e -> {

				if (houseMap.get(e.getHouseId()) != null) {
					UserHouseVo vo = new UserHouseVo();
					BeanUtils.copyProperties(e, vo);
					BeanUtils.copyProperties(houseMap.get(e.getHouseId()), vo);

					retList.add(vo);
				}

			});

		}

		return retList;
	}

	public Object reqAuthMyHouse(UserHouseVo vo) {

		vo.getHouseId();
		vo.getHouseId();

		UserHouse uh = new UserHouse();
		uh.setAuthStatu(0);

		uh.setId(vo.getId());

		Optional<UserHouse> uhOpt = userHouseDao.findOne(Example.of(uh));
		if (uhOpt.isPresent()) {
			uh = uhOpt.get();
			uh.setReqAuthDateTime(LocalDateTime.now());

			Random rand = new Random();

			int result = rand.nextInt(1000000);

			uh.setAuthCode("" + result);
			uh.setAuthStatu(1);
			userHouseDao.save(uh);

		}

		return null;
	}

	public Object removeMyHouse(House house) {

		Integer userId = SecurityUtils.getCurrentUserId();
		UserHouse userHouse = new UserHouse();
		userHouse.setHouseId(house.getHouseId());
		userHouse.setUserId(userId);
		List<UserHouse> exists = userHouseDao.findAll(Example.of(userHouse));
		userHouseDao.deleteAll(exists);
		return null;
	}





	public Object getAuthRequests() {

		UserHouse uh = new UserHouse();

		uh.setAuthStatu(1);

		List<UserHouse> userHouseList = userHouseDao.findAll(Example.of(uh));

		List<Integer> ids = userHouseList.stream().map(e -> e.getHouseId()).collect(Collectors.toList());

		Stream<House> houseListStream = houseDao.findAllById(ids).stream();

		Map<Integer, House> houseMap = new HashMap<Integer, House>();

		houseListStream.forEach(e -> {
			houseMap.put(e.getHouseId(), e);
		});

		ArrayList<UserHouseVo> retList = new ArrayList<UserHouseVo>();

		userHouseList.forEach(e -> {

			if (houseMap.get(e.getHouseId()) != null) {
				UserHouseVo vo = new UserHouseVo();
				BeanUtils.copyProperties(e, vo);
				BeanUtils.copyProperties(houseMap.get(e.getHouseId()), vo);

				retList.add(vo);
			}

		});
		
	
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("contents", retList);
		ret.put("userId", SecurityUtils.getCurrentUserId());
		return ret;
	}

	public Rest approveAuthReq(UserHouseVo vo) {
		
		int userId = SecurityUtils.getCurrentUserId().intValue();
		
		Optional<UserHouse> userHouseOpt = userHouseDao.findById(vo.getId());
		if(userHouseOpt.isPresent()) {
			UserHouse entity = userHouseOpt.get();
			if(entity.getAuthCode().equals(vo.getAuthCode())) {
				if(entity.getAppvUserIds()!=null && entity.getAppvUserIds().indexOf(""+userId)>-1) {
					return Rest.fail(ReturnCodeEnum.UNKNOWN_ERROR,"你已经通过了.");
				}else {
					entity.setAppvUserIds((entity.getAppvUserIds()==null?"":entity.getAppvUserIds()+",")+userId);
					entity.setAuthStatu(2);
					userHouseDao.save(entity);
					return Rest.success();
				}
			}else {
				return Rest.fail(ReturnCodeEnum.UNKNOWN_ERROR,"验证码与系统记录不符.");
			}
		}

		return Rest.fail(ReturnCodeEnum.UNKNOWN_ERROR,"找不到记录");
	}

}
