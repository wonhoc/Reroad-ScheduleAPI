package com.example.utill;

import org.springframework.beans.factory.annotation.Value;

//serviceKey 정보를 가져오는 class

public class ServiceKeyUtil {
	
	@Value("${serviceKey.train}")
	private String serviceKey;
	
	public String getServiceKey() {
		
		System.out.println("serviceKey = " + this.serviceKey);
		
		return this.serviceKey;
		
	}//serviceKey() end
	
	
}//class end
