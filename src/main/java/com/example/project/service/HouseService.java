/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.service;

import java.time.LocalDateTime;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.project.dao.HouseDao;
import com.example.project.dao.UserDao;
import com.example.project.dao.UserHouseAuthLogDao;
import com.example.project.dao.UserHouseDao;
import com.example.project.model.House;
import com.example.project.model.User;
import com.example.project.model.UserHouse;
import com.example.project.utils.Rest;
import com.example.project.utils.ReturnCodeEnum;
import com.example.project.utils.SecurityUtils;
import com.example.project.vobean.UserHouseVo;
import com.example.project.vobean.UserVo;

@Service
public class HouseService {

	@Autowired
	private HouseDao houseDao;

	@Autowired
	private UserHouseDao userHouseDao;

	@Autowired
	private UserHouseAuthLogDao userHouseAuthLogDao;

	@Autowired
	private UserDao userDao;

	public Object initHouses(List<House> list) {

		//houseDao.deleteAll();
		
		houseDao.saveAll(list);

		return null;
	}

	public List<House> getHouseList() {
		return houseDao.findAll();
	}

	public Object userBindHouses(List<House> houses) {
		Integer userId = SecurityUtils.getCurrentUserId();

		if (houses != null)
			for (House house : houses) {
				Optional<House> entityHouse = houseDao.findById(house.getHouseId());

				if (entityHouse.isPresent()) {
					UserHouse userHouse = new UserHouse();
					userHouse.setHouseId(house.getHouseId());
					userHouse.setUserId(userId);
					userHouse.setAuthStatu(UserHouse.NO_AUTH);
					List<UserHouse> exists = userHouseDao.findAll(Example.of(userHouse));
					if (exists.size() == 0) {
						userHouseDao.save(userHouse);
						House houseEo = entityHouse.get();
						if (houseEo.getBinderIds() == null)
							houseEo.setBinderIds("" + userId);
						else
							houseEo.setBinderIds(houseEo.getBinderIds() + "," + userId);
						
						houseDao.save(houseEo);
					}
				}
			}

		UserHouse userHouse = new UserHouse();
		userHouse.setUserId(userId);

		List<UserHouse> userHouses = userHouseDao.findAll(Example.of(userHouse));

		if (userHouses.size() > 0) {

			Random rand = new Random();

			int result = rand.nextInt(900000) + 100000;
			User user = userDao.findById(userId).get();
			user.setAuthNum(result);
			userDao.save(user);

			SecurityUtils.bindUserToSession(SecurityUtils.userToVo(user));
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

	public Object unBindUserHouses() {

		Integer userId = SecurityUtils.getCurrentUserId();

		UserHouse userHouse = new UserHouse();
		userHouse.setUserId(userId);
		List<UserHouse> userHouses = userHouseDao.findAll(Example.of(userHouse));

		for (UserHouse uh : userHouses) {
			House house = houseDao.findById(uh.getHouseId()).get();
			if(house.getBinderIds()!=null) {
				String[] ids = house.getBinderIds().split(",");
				StringBuilder sb = new StringBuilder();
				for (String id : ids) {
					if (!id.equals("") &&Integer.parseInt(id) != userId) {
						sb.append(",").append(id);
					}
				}
				if (sb.indexOf(",") == 0) {
					sb.delete(0, 1);
				}
				house.setBinderIds(sb.toString());
				houseDao.save(house);
			}

		}
		userHouseDao.deleteAll(userHouses);
		return null;
	}

	public Object getAuthRequests() {

		UserHouse uh = new UserHouse();

		uh.setAuthStatu(UserHouse.NO_AUTH);

		List<UserHouse> allUserHouseList = userHouseDao.findAll(Example.of(uh),Sort.by(Sort.Direction.DESC, "userId"));

		List<Integer> ids = allUserHouseList.stream().map(e -> e.getHouseId()).collect(Collectors.toList());

		Stream<House> houseListStream = houseDao.findAllById(ids).stream();

		Map<Integer, House> houseMap = new HashMap<Integer, House>();

		houseListStream.forEach(e -> {
			houseMap.put(e.getHouseId(), e);
		});


		int lastUserId=0;
		
		List<Map<String,Object>> retUser = new ArrayList<Map<String,Object>>();
		List<UserHouseVo> userHouseList=null;
		Map<String,Object> userMap = null;
		for(UserHouse userHouse:allUserHouseList) {
			if(userHouse.getUserId().intValue()!=lastUserId) {
				userHouseList = new ArrayList<UserHouseVo>();
				userMap = new HashMap<String,Object>();
				retUser.add(userMap);
				userMap.put("list", userHouseList);
				User user = userDao.findById(userHouse.getUserId()).get();

				
				UserVo userVo = new UserVo();
				userVo.setId(user.getUserId());
				userVo.setNickName(user.getNickName());
				userVo.setUserName(user.getUserName());
				userVo.setMask(true);

				userMap.put("user", userVo);

				lastUserId = userHouse.getUserId();

			}
			
			if (houseMap.get(userHouse.getHouseId()) != null) {
				UserHouseVo vo = new UserHouseVo();
				BeanUtils.copyProperties(userHouse, vo);
				BeanUtils.copyProperties(houseMap.get(userHouse.getHouseId()), vo);
				String[] userIds = houseMap.get(userHouse.getHouseId()).getBinderIds().split(",");
				vo.setBinderUsers(new ArrayList<UserVo>());
				for(String id:userIds) {
					if(!id.equals("")) {
						User user = userDao.findById(Integer.parseInt(id)).get();
						UserVo userVo = new UserVo();
						userVo.setId(user.getUserId());
						userVo.setNickName(user.getNickName());
						userVo.setUserName(user.getUserName());
						userVo.setMask(true);
						vo.getBinderUsers().add(userVo);
					}
				}
				userHouseList.add(vo);
			}
		}



		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("contents", retUser);
		ret.put("userId", SecurityUtils.getCurrentUserId());
		return ret;
	}

	public Rest approveAuthReq(UserHouseVo vo) {

		int userId = SecurityUtils.getCurrentUserId().intValue();

		Optional<UserHouse> userHouseOpt = userHouseDao.findById(vo.getId());
		if (userHouseOpt.isPresent()) {
			UserHouse entity = userHouseOpt.get();
			if (entity.getAuthCode().equals(vo.getAuthCode())) {
				if (entity.getAppvUserIds() != null && entity.getAppvUserIds().indexOf("" + userId) > -1) {
					return Rest.fail(ReturnCodeEnum.UNKNOWN_ERROR, "你已经通过了.");
				} else {
					entity.setAppvUserIds(
							(entity.getAppvUserIds() == null ? "" : entity.getAppvUserIds() + ",") + userId);
					entity.setAuthStatu(2);
					userHouseDao.save(entity);
					return Rest.success();
				}
			} else {
				return Rest.fail(ReturnCodeEnum.UNKNOWN_ERROR, "验证码与系统记录不符.");
			}
		}

		return Rest.fail(ReturnCodeEnum.UNKNOWN_ERROR, "找不到记录");
	}

}
