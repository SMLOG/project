/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.apiController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.kaptcha.Kaptcha;
import com.example.project.service.SendEmailService;
import com.example.project.service.UserService;
import com.example.project.utils.Rest;
import com.example.project.utils.ReturnCodeEnum;
import com.example.project.vobean.UserVo;

import cn.dev33.satoken.stp.StpUtil;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private SendEmailService emailService;
	
	 @Autowired
	  private Kaptcha kaptcha;
	 

	  @GetMapping("/api/public/captcha")
	  public void render() {
	    kaptcha.render();
	  }
	  
	  
	@RequestMapping(value="/api/public/reg", method = RequestMethod.POST) 
	public Rest registerUser(HttpServletRequest request,@RequestBody UserVo userVo) throws MessagingException {
		
		try {
			kaptcha.validate(userVo.getCaptcha());
		    	
		  
		}catch(Exception e) {
	    	return Rest.fail(ReturnCodeEnum.UNKNOWN_ERROR, "验证码错误!");

		}

		int  code = new Random().nextInt(9000000)+1000000;
		
		
		emailService.sendHtmlEmail(userVo.getUserName(),"验证你的账号", String.format( "请点击下面链接验证你的<a href=\"http://localhost:8082/project/public/verify?id=%d\">http://localhost:8082/project/public/verify?id=%d</a>",code,code));
	    
		request.getServletContext().setAttribute(""+code, userVo);
		
		
		return Rest.success();
		

	}
	
	@RequestMapping(value="/api/public/verify", method = RequestMethod.GET) 
	public Rest veryfyEmail(HttpServletRequest req,@RequestParam(name="id") String code) {
		
		UserVo userVo = (UserVo) req.getServletContext().getAttribute(code);
		Rest status = userService.createUser(userVo);
		
		if(status.isOK()) {
			
			return login2(userVo);

		}
		else return status;
	}
    @PostMapping("isLogin")
    public Rest isLogin() {
        return Rest.success(StpUtil.isLogin());
    }

    @PostMapping("/api/public/login")
    public Rest login(@RequestBody @Valid UserVo userVo) {
    	
		try {
			kaptcha.validate(userVo.getCaptcha());
		    	
		  
		}catch(Exception e) {
	    	return Rest.fail(ReturnCodeEnum.UNKNOWN_ERROR, "验证码错误!");

		}

	    
        return login2(userVo);

    }


	private Rest login2(UserVo userVo) {
		UserVo user = userService.login(userVo);
        if (user != null) {
            StpUtil.login(user.getUserName(),true);
            Map<String, Object> result = new HashMap<>();
            result.put("token", StpUtil.getTokenInfo());
            result.put("user", user);
            StpUtil.getSession().set("user", user);
            return Rest.success(result);
        }
		return Rest.fail(ReturnCodeEnum.USER_NAME_OR_PWD_ERROR);
	}

    @GetMapping("/api/loginUser")
    public Rest loginUser() {
    	if(StpUtil.isLogin()){
        	UserVo user = (UserVo) StpUtil.getSession().get("user");

    		return Rest.success(user);

    	}else

		return Rest.fail(ReturnCodeEnum.USER_LOGIN_ERROR,"error");

    }
    
    @GetMapping("/api/logout")
    public Rest logout() {

        StpUtil.logout();
        return Rest.success();
    }
    @PostMapping("/api/changePass")
    public Rest changePass(@RequestBody @Valid UserVo userVo) {
    		
    	return userService.changePass(userVo);

    }
    
}
