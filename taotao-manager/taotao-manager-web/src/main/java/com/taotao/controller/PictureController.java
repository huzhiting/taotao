package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.pojo.PictureResult;
import com.taotao.service.PictureService;
import com.taotao.utils.JsonUtils;

/**
 * 图片上传controller
 * @author hzt
 *
 */
@Controller
public class PictureController {
	
	@Autowired
	private PictureService pictureService;
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile){
		PictureResult result=pictureService.uploadPic(uploadFile);
		//需要把Java对象手工转换为json数据
		String json=JsonUtils.objectToJson(result);
		return json;	
	}
	

}
