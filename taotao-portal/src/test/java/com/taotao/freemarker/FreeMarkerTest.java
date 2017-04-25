package com.taotao.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTest {

	public class Student{
		private int id;
		private String name;
		private String address;
		
		public Student(int id,String name,String address){
			this.id=id;
			this.name=name;
			this.address=address;
		}
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		
	}
	
	@Test
	public void testFreeMarker() throws Exception{
		//		第一步：把freemarker的jar包添加到工程中
		//		第二步：freemarker的运行不依赖web容器，可以在java工程中运行。创建一个测试方法进行测试。
		//		第三步：创建一个Configration对象
		Configuration configuration=new Configuration(Configuration.getVersion());
		//		第四步：告诉config对象模板文件存放的路径。
		configuration.setDirectoryForTemplateLoading(new File("D:\\taotaoworkspace\\taotao-portal\\src\\main\\webapp\\WEB-INF\\ftl"));
		//		第五步：设置config的默认字符集。一般是utf-8
		configuration.setDefaultEncoding("utf-8");
		//		第六步：从config对象中获得模板对象。需要制定一个模板文件的名字。
		//Template template = configuration.getTemplate("first.ftl");
		Template template = configuration.getTemplate("second.ftl");
		//		第七步：创建模板需要的数据集。可以是一个map对象也可以是一个pojo，把模板需要的数据都放入数据集。
		Map rootMap =new HashMap<>();
		//rootMap.put("hello", "hello freemarker");
		rootMap.put("title", "hello freemarker");
		rootMap.put("student", new Student(1, "张三", "北京"));
		List<Student> stuList=new ArrayList<>();
		stuList.add(new Student(1, "张三", "北京"));
		stuList.add(new Student(2, "张三2", "北京2"));
		stuList.add(new Student(3, "张三3", "北京3"));
		stuList.add(new Student(4, "张三4", "北京4"));
		stuList.add(new Student(5, "张三5", "北京5"));
		rootMap.put("students", stuList);
		//		第八步：创建一个Writer对象，指定生成的文件保存的路径及文件名。
		//Writer outWriter =new FileWriter(new File("D:\\temp\\html\\hello.html"));
		Writer outWriter =new FileWriter(new File("D:\\temp\\html\\second.html"));
		//		第九步：调用模板对象的process方法生成静态文件。需要两个参数数据集和writer对象。
		template.process(rootMap, outWriter);
		//		第十步：关闭writer对象。
		outWriter.flush();
		outWriter.close();
	}
}
