/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.dao;

import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.model.UserHouse;
import com.example.project.utils.Rest;
import com.example.project.utils.RestException;

@Repository
public interface UserHouseDao  extends JpaRepository<UserHouse,Integer>{

	default int getUserTypeTicks(Integer userType, Integer userId,String name) throws Exception {
		

		if(userType.intValue()==0)return 1;
		else if(userType.intValue()>0) {
			
			UserHouse uh = new UserHouse();
			uh.setUserId(userId);
			
			if (userType.intValue() > 2 ) {
				
				uh.setAuthStatu(UserHouse.AUTH_2);
				long count = this.count(Example.of(uh));
				if(count<=0) throw new RestException(Rest.fail("只有验证房产业主才能"+name+"，请先绑定和申请验证你的房子."));
				
				return (int) (userType.intValue()==3?count:userType.intValue()==4?1:0);
			}else {
				long count = this.count(Example.of(uh));
				if(count<=0) throw new RestException(Rest.fail("只有业主才能"+name+"，请先绑定你的房子."));
				return (int) (userType.intValue()==1?count:userType.intValue()==2?1:0);

			}
			
		}

		throw new RestException(Rest.fail("非法数据."));
		
	}

	default String getUserHouseIds(Integer userId,int userType) {
		UserHouse uh = new UserHouse();
		uh.setUserId(userId);
		
		if (userType  > 2 ) {
		
			uh.setAuthStatu(UserHouse.AUTH_2);
			
		}
		return this.findAll(Example.of(uh)).stream().map(e->e.getHouseId().toString()).collect(Collectors.joining(","));
	}

}
