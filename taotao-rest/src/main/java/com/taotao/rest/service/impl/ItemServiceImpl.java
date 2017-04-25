package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.component.JedisClient;
import com.taotao.rest.service.ItemService;
import com.taotao.utils.JsonUtils;

/**
 * 商品管理Service
 * @author hzt
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private TbItemParamItemMapper itemParamMapper;
	
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY; //商品
	@Value("${ITEM_BASE_INFO_KEY}")
	private String ITEM_BASE_INFO_KEY; //商品基本信息
	@Value("${ITEM_EXPIRE_SECOND}")
	private Integer ITEM_EXPIRE_SECOND; //过期时间
	@Value("${ITEM_DESC_KEY}")
	private String ITEM_DESC_KEY; //商品描述
	@Value("${ITEM_PARAM_KEY}")
	private String ITEM_PARAM_KEY; //商品规格参数
	
	@Override
	public TbItem getItemById(Long itemId) {
		//查询缓存，如果有缓存，直接返回
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_BASE_INFO_KEY);
			//判断数据是否存在
			if(StringUtils.isNotBlank(json)){
				//把json转换成java对象
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//根据商品id查询商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		
		//向redis添加缓存
		try {
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_BASE_INFO_KEY, JsonUtils.objectToJson(item));
			//设置key过期时间
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" +  ITEM_BASE_INFO_KEY, ITEM_EXPIRE_SECOND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		//查询缓存，如果有缓存，直接返回
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" +  itemId + ":" +ITEM_DESC_KEY);
			//判断数据是否存在
			if(StringUtils.isNotBlank(json)){
				//把json转换成java对象
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//根据商品id查询商品描述信息
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		//向redis添加缓存
		try {
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId  + ":" + ITEM_DESC_KEY, JsonUtils.objectToJson(itemDesc));
			//设置key过期时间
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_DESC_KEY , ITEM_EXPIRE_SECOND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

	@Override
	public TbItemParamItem getItemParamById(Long itemId) {
		//添加缓存逻辑
		//查询缓存，如果有缓存，直接返回
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY);
			//判断数据是否存在
			if (StringUtils.isNotBlank(json)) {
				// 把json数据转换成java对象
				TbItemParamItem itemParamitem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
				return itemParamitem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 根据商品id查询规格参数
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = itemParamMapper.selectByExampleWithBLOBs(example);
		//取规格参数
		if (list != null && list.size() > 0) {
			TbItemParamItem itemParamItem = list.get(0);
			//添加缓存
			try {
				//向redis中添加缓存
				jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY
						, JsonUtils.objectToJson(itemParamItem));
				//设置key的过期时间
				jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY, ITEM_EXPIRE_SECOND);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return itemParamItem;
		}
		return null;
	}
	
}

