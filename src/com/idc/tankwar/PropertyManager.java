package com.idc.tankwar;
import java.io.IOException;
import java.util.Properties;
/**
 * ���ڴ������ļ��ж�ȡ������Ϣ����ȡ����ģʽ
 * @author xxq
 *
 */
public class PropertyManager {
	
	private static Properties props = new Properties();
	
	static {
		//�������ļ�һ����load()���ڴ�֮�󣬾�һֱ������ڴ��У��Ժ�Ϳ���ֱ�Ӵ��ڴ��
		try {
			props.load(PropertyManager.class.getClassLoader().getResourceAsStream("config/tank.Properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//�����췽������Ϊ˽�еģ��������ڱ������newPropertyManager�Ķ���
	private PropertyManager() {}
	
	public static String getProperty(String key) {
		return props.getProperty(key);
	}
	
}
