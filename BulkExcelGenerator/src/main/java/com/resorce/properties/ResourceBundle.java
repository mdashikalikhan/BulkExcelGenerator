package com.resorce.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceBundle {
	private Properties properties;
	
	public ResourceBundle() {
		properties = new Properties();
		ClassLoader objClassLoader=null;
		objClassLoader = Thread.currentThread().getContextClassLoader();
		InputStream is = objClassLoader.getResourceAsStream("config.properties");
		if(is!=null){			
			try {
				properties.load(is);				
			} catch (IOException e) {
								
			} finally {
				try {
					is.close();
				} catch (IOException e) {					
					
				}
			}
		}
	}
	
	public String getValue(String key) {
		return properties.getProperty(key);
	}
	
	

}
