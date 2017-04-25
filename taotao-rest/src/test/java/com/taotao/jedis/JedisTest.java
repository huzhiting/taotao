package com.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import com.taotao.rest.component.JedisClient;

public class JedisTest {
	
	@Test
	public void testJedisSingle() throws Exception{
		//创建一个jedis对象
		Jedis jedis =new Jedis("192.168.59.130",6379);
		jedis.set("test", "hello jedis");
		String string =jedis.get("test");
		System.out.print(string);
		jedis.close();
	}
	
	
	//使用连接池
	@Test
	public void testJedisPool() throws Exception {
		//创建一个连接池对象
		//系统中应该是单例的。
		JedisPool jedisPool = new JedisPool("192.168.59.130", 6379);
		//从连接池中获得一个连接
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get("test");
		System.out.println(result);
		//jedis必须关闭
		jedis.close();
		
		//系统关闭时关闭连接池
		jedisPool.close();
		
	}
	

	@Test
	public void testJedisCluster() throws Exception {
		//创建一个JedisCluster对象
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.59.130", 7000));
		nodes.add(new HostAndPort("192.168.59.130", 7001));
		nodes.add(new HostAndPort("192.168.59.130", 7002));
		nodes.add(new HostAndPort("192.168.59.130", 7003));
		nodes.add(new HostAndPort("192.168.59.130", 7004));
		nodes.add(new HostAndPort("192.168.59.130", 7005));

		//在nodes中指定每个节点的地址
		//jedisCluster在系统中是单例的。
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("name", "zhangsan");
		jedisCluster.set("value", "100");
		String name = jedisCluster.get("name");
		String value = jedisCluster.get("value");
		System.out.println(name);
		System.out.println(value);	
		
		//系统关闭时关闭jedisCluster
		jedisCluster.close();
	}
	
	@Test
	public void testJedisClientSpring() throws Exception {
		//创建一个spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		//从容器中获得JedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		//jedisClient操作redis
		jedisClient.set("cliet1", "1000");
		String string = jedisClient.get("cliet1");
		System.out.println(string);
	}

}
