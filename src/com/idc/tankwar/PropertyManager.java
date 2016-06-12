package com.idc.tankwar;
import java.io.IOException;
import java.util.Properties;
/**
 * 用于从配置文件中读取配置信息，采取单例模式
 * @author xxq
 *
 */
public class PropertyManager {
	
	private static Properties props = new Properties();
	
	static {
		//该配置文件一旦被load()进内存之后，就一直存放在内存中，以后就可以直接从内存读
		try {
			props.load(PropertyManager.class.getClassLoader().getResourceAsStream("config/tank.Properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//将构造方法设置为私有的，不允许在别的类中newPropertyManager的对象
	private PropertyManager() {}
	
	public static String getProperty(String key) {
		return props.getProperty(key);
	}
	
}
