package com.taotao.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.sso.service.LogoutService;
import com.taotao.utils.ExceptionUtil;

@Controller
public class LogoutController {
	
	@Autowired 
	private LogoutService logoutService;
	
	@RequestMapping("/user/logout")
	@ResponseBody
	public TaotaoResult logout(@PathVariable String token){
		try {
			TaotaoResult result = logoutService.delToken(token); 
			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

}
