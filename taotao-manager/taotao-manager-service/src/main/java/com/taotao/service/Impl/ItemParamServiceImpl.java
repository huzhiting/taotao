package com.taotao.service.Impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamExample.Criteria;
import com.taotao.service.ItemParamService;

/**
 * 商品规格参数模板管理Service
 * @author hzt
 *
 */
@Service
public class ItemParamServiceImpl implements ItemParamService {

	@Autowired
	private TbItemParamMapper itemParamMapper;
	
	@Override
	public TaotaoResult getItemParamByCid(Long cid) {
		//根据cid查询规格参数模板
		TbItemParamExample example=new TbItemParamExample();
		Criteria criteria=example.createCriteria();
		criteria.andItemCatIdEqualTo(cid);
		//执行查询
		List<TbItemParam> list=itemParamMapper.selectByExampleWithBLOBs(example);
		//判断是否查询到结果
		if(list!=null && !list.isEmpty()){
			TbItemParam itemParam=list.get(0);
			return TaotaoResult.ok(itemParam);
		}
		return TaotaoResult.ok();
	}

	/**
	 * 查询规格参数列表
	 */
	@Override
	public EasyUIDataGridResult getItemParamList(int page, int rows) {
		//分页处理
		PageHelper.startPage(page,rows);
		//执行查询
		TbItemParamExample example = new TbItemParamExample();		
		List<TbItemParam> list=itemParamMapper.selectByExampleWithBLOBs(example);
		//取分页信息
		PageInfo<TbItemParam> pageInfo=new PageInfo<>(list);
		
		EasyUIDataGridResult result=new EasyUIDataGridResult();
		//返回结果处理
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);

		return result;
	}

	/**
	 * 保存规格参数
	 */
	@Override
	public TaotaoResult insertItemParam(Long cid, String paramData) {
		//创建一个pojo
		TbItemParam itemParam =new TbItemParam();
		itemParam.setCreated(new Date());
		itemParam.setUpdated(new Date());
		itemParam.setItemCatId(cid);
		itemParam.setParamData(paramData);
		//插入记录
		itemParamMapper.insert(itemParam);
		return TaotaoResult.ok();
	}

	/**
	 * 删除规格参数
	 */
	@Override
	public TaotaoResult deleteItemParamByIds(List<Long> idList) {
		TbItemParamExample example=new TbItemParamExample();
		Criteria criteria=example.createCriteria();
		criteria.andIdIn(idList);
		itemParamMapper.deleteByExample(example);
		return TaotaoResult.ok();
	}

}
