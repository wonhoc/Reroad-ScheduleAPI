package com.example.ReroadScheduleAPI;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootConfiguration
@SpringBootTest
class ReroadScheduleApiApplicationTests {
	
	
	
	@Test
	void contextLoads(){
		
		
		String Ddate = "202201110640"; 	
		String Adate = "202201110840";
		
		int DdateH = Integer.parseInt(Ddate.substring(8, 10));
		int DdateM = Integer.parseInt(Ddate.substring(10, 12));
		
		System.out.println("DdateH : " + DdateH);
		System.out.println("DdateM : " + DdateM);
		
		int AdateH = Integer.parseInt(Adate.substring(8, 10));
		int AdateM = Integer.parseInt(Adate.substring(10, 12));
		
		System.out.println("AdateH : " + AdateH);
		System.out.println("AdateM : " + AdateM);
		
		int h = (((AdateH  * 60) + AdateM) - ((DdateH * 60) + DdateM)) / 60;
		int m = (((AdateH  * 60) + AdateM) - ((DdateH * 60) + DdateM)) % 60;
		System.out.println(h);
		System.out.println(m);
		
		String spanTime = h + "시간" + m + "분";
		
		System.out.println(spanTime);
		 
	}//test() end
	
	@Test @Disabled
	void test (){
		
		String id = "NAEK010";
		
		String subId = id.substring(4, 5);
		
		System.out.println(subId);
	}

}//class end
