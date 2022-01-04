package com.example.service;

import java.util.ArrayList;

import com.example.vo.CityInfoVo;
import com.example.vo.expBusVo.ExpBusScVo;
import com.example.vo.expBusVo.ExpBusSetInfoVo;
import com.example.vo.expBusVo.ExpBusTmlInfoVo;

public interface ExpbusService {
	
	ArrayList<CityInfoVo> getCityCode() throws Exception;
	ArrayList<ExpBusTmlInfoVo> getExpBusTmlInfo() throws Exception;
	ArrayList<ExpBusTmlInfoVo> allExpBusList();
	ExpBusScVo getExpBusSc(ExpBusSetInfoVo settings) throws Exception;

}//interface end
