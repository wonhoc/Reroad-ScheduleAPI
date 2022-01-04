package com.example.service;

import java.util.ArrayList;

import com.example.vo.CityInfoVo;
import com.example.vo.trainVo.CityTrainStInfoVo;
import com.example.vo.trainVo.TrainScVo;
import com.example.vo.trainVo.TrainSetInfoVo;
import com.example.vo.trainVo.TrainStInfoVo;

public interface TrainService {

	 ArrayList<CityInfoVo> getCityCode() throws Exception;
	 ArrayList<TrainStInfoVo> getTrainStInfo(String cityCode) throws Exception;
	 ArrayList<CityTrainStInfoVo> getCityTrainStInfo();
	 TrainScVo getTrainSc(TrainSetInfoVo settings) throws Exception;
	 
}//interface() end
