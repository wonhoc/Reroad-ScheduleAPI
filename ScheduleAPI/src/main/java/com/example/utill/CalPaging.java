package com.example.utill;

import java.util.HashMap;
import java.util.Map;

/*
 
  hateoas 설정에필요한 페이징 정보 return class
 
 */

public class CalPaging {
	
	public static Map paging(String nowPage, String totalCnt, String pageOfRows) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		int inowPage = Integer.parseInt(nowPage);
		int itotalCnt = Integer.parseInt(totalCnt);
		int ipageOfRows = Integer.parseInt(pageOfRows);
		
		String nextPage;
		String lastPage;
		
		if(inowPage == 1) {
			
			nextPage = Integer.toString(inowPage + 1);
			map.put("nextPage", nextPage);
			
		}else if(inowPage  * ipageOfRows >= itotalCnt){
			lastPage = Integer.toString(inowPage - 1);
			map.put("lastPage", lastPage);
			
		}else {
			nextPage = Integer.toString(inowPage + 1);
			lastPage = Integer.toString(inowPage - 1);
			map.put("nextPage", nextPage);
			map.put("lastPage", lastPage);
		}//if end
		
		return map;

	}//paging() end

	

}//class end
