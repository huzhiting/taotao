package com.taotao.portal.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;
import com.taotao.utils.HttpClientUtil;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Value("${SEARCH_BASE_URL}")
	private String SEARCH_BASE_URL;
	
	@Override
	public SearchResult search(String keyword, int page, int rows) {
		//调用服务查询商品列表
		Map<String, String> param=new HashMap<>();
		param.put("keyword",keyword);
		param.put("page", page+"");
		param.put("rows", rows+"");
		//调用服务
		String json = HttpClientUtil.doGet(SEARCH_BASE_URL,param);
		//转换成java对象
		TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, SearchResult.class);
		//取得返回的对象
		SearchResult searchResult = (SearchResult)taotaoResult.getData();
		
		return searchResult;
	}

}
