package com.taotao.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ItemParamService;

/**
 * 商品
 * @author hzt
 *
 */
@Controller
@RequestMapping("/item/param")
public class ItemParamController {

	@Autowired
	private ItemParamService itemParamService;
	
	/**
	 * 查询规格参数列表
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		EasyUIDataGridResult result=itemParamService.getItemParamList(page, rows);
		return result;
	}
	
	
	@RequestMapping("/query/itemcatid/{cid}")
	@ResponseBody
	public TaotaoResult getItemCatByCid(@PathVariable Long cid){
		TaotaoResult result=itemParamService.getItemParamByCid(cid);
		return result;
	}
	
	@RequestMapping("/save/{cid}")
	@ResponseBody
	public TaotaoResult insertItemParam(@PathVariable Long cid,String paramData){
		TaotaoResult result=itemParamService.insertItemParam(cid, paramData);
		return result;
	}
	
	@RequestMapping("/delete")
	public TaotaoResult deleteItemParamById(String ids){
		ids=ids+",";
		List<Long> idList=new ArrayList<>();	
		String[] id=ids.split(",");
		for(int i=0;i<id.length;i++){
			idList.add(Long.parseLong(id[i]));
		}			
		TaotaoResult result=itemParamService.deleteItemParamByIds(idList);
		return result;
	}
	
}
