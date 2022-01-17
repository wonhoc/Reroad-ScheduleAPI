package com.example.vo.expBusVo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpBusTmlInfoVo {

	private String tmId;	//터미널 ID
	private String tmName;  //터미널 이름
	private String city;	//터미널이 있는 도시
	
	
	public ExpBusTmlInfoVo(String tmId, String tmName) {
		this.tmId = tmId;
		this.tmName = tmName;
	}
	
}//class end
