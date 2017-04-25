package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	
	TaotaoResult insertContent(TbContent content);

	EasyUIDataGridResult queryContentList(Long categoryId,Integer page, Integer rows);

}
