package com.taotao.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.sso.component.JedisClient;
import com.taotao.sso.service.LogoutService;


/**
 * 安全退出service
 * @author hzt
 *
 */
@Service
public class LogoutServiceImpl implements LogoutService {

	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_SESSION_KEY}")
	private String REDIS_SESSION_KEY;
	
	@Override
	public TaotaoResult delToken(String token) {
		//根据token取用户信息
		jedisClient.decr(REDIS_SESSION_KEY + ":" + token);
		return TaotaoResult.ok();
	}

}
