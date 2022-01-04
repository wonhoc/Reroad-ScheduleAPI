package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.vo.CityInfoVo;
import com.example.vo.expBusVo.ExpBusTmlInfoVo;
import com.example.vo.trainVo.TrainStInfoVo;

@Repository("cityInfoDao")
public class CityInfoDaoImpl implements CityInfoDao{

	@Autowired
	private SqlSession sqlSession;
	
	private static final String NAME_SP = "Info.";
	
	//api에서 제공하는 도시이름과 도시 코드를 입력
	@Override
	public void insertCityInfo(CityInfoVo cityInfo) {
		System.out.println("begin");
		sqlSession.insert(NAME_SP + "insertCityInfo", cityInfo);		
		System.out.println("done");
	}//insertCityInfo() end
	
	//api에서 제공하는 도시별 역 정보를 입력.
	@Override
	public void insertTrainStInfo(TrainStInfoVo TrainStInfo) {
		System.out.println("begin");
		sqlSession.insert(NAME_SP + "insertTrainStInfo", TrainStInfo);
		System.out.println("done");
	}//insertTrainStInfo() end
	
	//도시 이름과 도시 코드를 조회
	@Override
	public List<CityInfoVo> selectCityInfo() {
		
		return sqlSession.selectList(NAME_SP + "selectCityInfoList");
		
	}//selectCityInfo() end
	
	//특정한 도시에 있는 모든 역정보 조회
	@Override
	public List<TrainStInfoVo> selectTrainStinfo(String cityCode) {
		
		return sqlSession.selectList(NAME_SP + "selectTrainStinfo", cityCode);
		
	}//selectCityTrainStinfo() end
	
	
	//전국에 있는 고속버스 터미널 목록 입력
	@Override
	public void insertExpTmlInfo(ExpBusTmlInfoVo expBusTmlInfo){
		
		sqlSession.insert(NAME_SP + "insertExpBusTml", expBusTmlInfo);
		
	}//insertExpTmlInfo end

	@Override
	public List<ExpBusTmlInfoVo> selectExpBusTmlInfo() {
		
		return sqlSession.selectList(NAME_SP + "selectTmlInfoList");
	}
	
	//전국에 있는 고속버스 터미널 목록 조회
	
	
	
	
}//class end
