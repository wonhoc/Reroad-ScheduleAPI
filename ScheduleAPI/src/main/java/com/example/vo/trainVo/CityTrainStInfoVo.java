package com.example.vo.trainVo;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CityTrainStInfoVo {
	
	String cityName;
	ArrayList<TrainStInfoVo> trainStInfo;
	
}//class end
