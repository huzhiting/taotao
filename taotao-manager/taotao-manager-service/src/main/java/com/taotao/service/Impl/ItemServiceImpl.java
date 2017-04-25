package com.taotao.service.Impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.service.ItemService;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	
	@Override
	public TbItem getItemById(Long itemId) {
		//TbItem item = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		//创建查询条件
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		//判断list中是否为空
		TbItem item = null;
		if (list != null && !list.isEmpty()) {
			item = list.get(0);
		}
		return item;
	}
	
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//分页处理
		PageHelper.startPage(page,rows);
		//执行查询
		TbItemExample example = new TbItemExample();		
		List<TbItem> list=itemMapper.selectByExample(example);
		//取分页信息
		PageInfo<TbItem> pageInfo=new PageInfo<>(list);
		
		EasyUIDataGridResult result=new EasyUIDataGridResult();
		//返回结果处理
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);

		return result;
	}

	@Override
	public TaotaoResult createItem(TbItem item, String desc,String itemParam) {
		//使用工具类生成商品id
		long itemId=IDUtils.genItemId();
		//补全商品属性
		//商品id
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		//创建及更新时间
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//插入商品表
		itemMapper.insert(item);
		//商品描述
		TbItemDesc itemDesc=new TbItemDesc();
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		//插入商品描述表
		itemDescMapper.insert(itemDesc);	
		
		//添加规格参数
		TbItemParamItem itemParamItem=new TbItemParamItem();
		itemParamItem.setCreated(new Date());
		itemParamItem.setUpdated(new Date());
		itemParamItem.setItemId(itemId);
		itemParamItem.setParamData(itemParam);
		itemParamItemMapper.insert(itemParamItem);
		
		return TaotaoResult.ok();
	}

	@Override
	public String getItemParamHtml(Long itemId) {
		//根据商品id查询规格参数
		TbItemParamItemExample example=new TbItemParamItemExample();
		//创建查询条件
		com.taotao.pojo.TbItemParamItemExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list=itemParamItemMapper.selectByExampleWithBLOBs(example);
		if(list==null || list.isEmpty()){
			return "";
		}
		TbItemParamItem itemParamItem=list.get(0);
		//取json数据
		String paramData=itemParamItem.getParamData();
		//转换为java对象
		List<Map> mapList=JsonUtils.jsonToList(paramData, Map.class);
		//遍历list生成html
		StringBuffer sb = new StringBuffer();	
		sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
		sb.append("	<tbody>\n");
		for (Map map : mapList) {
			sb.append("		<tr>\n");
			sb.append("			<th class=\"tdTitle\" colspan=\"2\">"+map.get("group")+"</th>\n");
			sb.append("		</tr>\n");
			//取规格项
			List<Map>mapList2 = (List<Map>) map.get("params");
			for (Map map2 : mapList2) {
				sb.append("		<tr>\n");
				sb.append("			<td class=\"tdTitle\">"+map2.get("k")+"</td>\n");
				sb.append("			<td>"+map2.get("v")+"</td>\n");
				sb.append("		</tr>\n");
			}
		}
		sb.append("	</tbody>\n");
		sb.append("</table>");
		
		return sb.toString();
	}

}
