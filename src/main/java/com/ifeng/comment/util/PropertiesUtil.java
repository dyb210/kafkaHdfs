package com.ifeng.comment.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	private static Properties prop = null;

	private static  PropertiesUtil pu = null ;
	
	private PropertiesUtil()  {
		
	}
	public static PropertiesUtil getProperties(String path){
		if(prop  == null)
		prop = new Properties();
		InputStream in = null;
		if(in  == null)
			try {
			in = new FileInputStream(new File(path));
			prop.load(in);
			in.close();
		} catch (IOException e) {
			
		}finally{
			if(in != null){
				in = null;
			}
		}
		if(pu == null){
			pu = new PropertiesUtil();
		}
		     return pu;
	}
	
	public String getParam(String  param){
		if(prop != null){
			return  prop.getProperty(param).trim();
		}
		return null;
	}
	
}


