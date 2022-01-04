package com.example.dao;

import java.util.List;

import com.example.vo.CityInfoVo;
import com.example.vo.expBusVo.ExpBusTmlInfoVo;
import com.example.vo.trainVo.TrainStInfoVo;

public interface CityInfoDao {

	
	void insertCityInfo(CityInfoVo cityInfo);
	void insertTrainStInfo(TrainStInfoVo TrainStInfo);
	List<CityInfoVo> selectCityInfo();
	List<TrainStInfoVo> selectTrainStinfo(String cityCode);
	void insertExpTmlInfo(ExpBusTmlInfoVo expBusTmlInfo);
	List<ExpBusTmlInfoVo> selectExpBusTmlInfo();
	
}//interface end
