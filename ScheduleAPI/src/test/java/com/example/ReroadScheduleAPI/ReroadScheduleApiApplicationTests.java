package com.example.ReroadScheduleAPI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootConfiguration
@SpringBootTest
class ReroadScheduleApiApplicationTests {
	
	
	
	@Test 
	void contextLoads(){
		
		
		String Ddate = "20201231140000";
		String Adate = "20201231173000";
		
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

	}//test() end

}//class end
