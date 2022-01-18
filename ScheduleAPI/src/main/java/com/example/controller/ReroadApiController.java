package com.example.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.config.hatosConfig.ExpBusInfoAsembler;
import com.example.config.hatosConfig.TrainInfoAssembler;
import com.example.dao.CityInfoDao;
import com.example.service.ExpbusService;
import com.example.service.TrainService;
import com.example.utill.CalPaging;
import com.example.vo.expBusVo.ExpBusScVo;
import com.example.vo.expBusVo.ExpBusSetInfoVo;
import com.example.vo.expBusVo.ExpBusTmlInfoVo;
import com.example.vo.trainVo.CityTrainStInfoVo;
import com.example.vo.trainVo.TrainScVo;
import com.example.vo.trainVo.TrainSetInfoVo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api")
public class ReroadApiController {
	
	@Autowired
	private TrainInfoAssembler trainInfoAssembler;
	
	@Autowired
	private ExpBusInfoAsembler expBusInfoAsembler;
	
	@Autowired
	private TrainService trainService;
	
	@Autowired
	private ExpbusService expbusService;

	
	@ApiOperation(value = "도시별 기차역 정보", notes = "성공시 도시코드별 기차역 ID와 기차역 이름을 반환한다. ")
	@GetMapping("/citytrainstinfo")
	public CollectionModel<EntityModel<CityTrainStInfoVo>> citytrainstinfo() {

		ArrayList<CityTrainStInfoVo> CityTrainStInfoList = this.trainService.getCityTrainStInfo();
		
		return this.trainInfoAssembler.toCollectionModel(CityTrainStInfoList);
		
	}//home() end
	
	@ApiOperation(value = "열차 운행 스케줄조회", notes = "성공시 사용자가 요청한 시간에 맞춰 출발지와 도착지에 해당하는 열차 운행 스케줄을 반환합니다. ")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pageNo", value = "페이지번호"),
		@ApiImplicitParam(name = "depPlaceId", value = "도착지 열차역 ID"),
		@ApiImplicitParam(name = "arrPlaceId", value = "출발지 열차역 ID"),
		@ApiImplicitParam(name = "depPlandTime", value = "조회 날짜")	
	})
	@GetMapping("/retrivetrainschedule/{pageNo}/{depPlaceId}/{arrPlaceId}/{depPlandTime}")
	public EntityModel<TrainScVo> retrivetrainschedule(@PathVariable String pageNo,
													   @PathVariable String depPlaceId,
													   @PathVariable String arrPlaceId,
													   @PathVariable String depPlandTime												  
														) throws Exception{
		@Valid
		TrainSetInfoVo settings = new TrainSetInfoVo(pageNo, depPlaceId, arrPlaceId, depPlandTime);
		
		TrainScVo trainSc = null;
		
		trainSc = this.trainService.getTrainSc(settings);
		
		return EntityModel.of(trainSc,			
				linkTo(methodOn(ReroadApiController.class).retrivetrainschedule(pageNo, depPlaceId, arrPlaceId, depPlandTime)).withSelfRel());

	}//retrivetrainschedule() end
	
	@ApiOperation(value = "고속버스 터미널 조회", notes = "성공시 터미널 이름과 터미널 ID를 반환합니다. ")
	@GetMapping("/expbustmlinfo")
	public CollectionModel<EntityModel<ExpBusTmlInfoVo>> expbustmlinfo(){
		
		ArrayList<ExpBusTmlInfoVo> expBusTmlInfoList = new ArrayList<ExpBusTmlInfoVo>();
		
		try {
			
			expBusTmlInfoList = this.expbusService.allExpBusList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return expBusInfoAsembler.toCollectionModel(expBusTmlInfoList);
		
	}//expbustmlinfo() end
	
	
	@ApiOperation(value = "고속버스 운행 스케줄 조회", notes = "성공시 사용자가 요청한 시간에 맞춰 출발지와 도착지에 해당하는 운행 정보들을 반환합니다. ")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "pageNo", value = "페이지번호"),
		@ApiImplicitParam(name = "depTerminalId", value = "도착지 터미널 ID"),
		@ApiImplicitParam(name = "arrTerminalId", value = "출발지 터미널 ID"),
		@ApiImplicitParam(name = "depPlandTime", value = "조회 날짜")	
	})
	@GetMapping("/retriveexpbusschedule/{depTerminalId}/{arrTerminalId}/{depPlandTime}/{pageNo}")
	public EntityModel<ExpBusScVo> retriveexpbusschedule(@PathVariable String depTerminalId,
														 @PathVariable String arrTerminalId,
														 @PathVariable String depPlandTime,
														 @PathVariable String pageNo
														) throws Exception{
		
		ExpBusSetInfoVo settings = new ExpBusSetInfoVo(depTerminalId, arrTerminalId, depPlandTime, pageNo);
		
		ExpBusScVo expBusSc = this.expbusService.getExpBusSc(settings);
		
		Map<String, String> paging = CalPaging.paging(pageNo, expBusSc.getTotalCnt(), expBusSc.getNumOfRows());
		
		
				
		return EntityModel.of(expBusSc,		
				linkTo(methodOn(ReroadApiController.class).retriveexpbusschedule(depTerminalId, arrTerminalId, depPlandTime, "1")).withRel("first"),
				linkTo(methodOn(ReroadApiController.class).retriveexpbusschedule(depTerminalId, arrTerminalId, depPlandTime, paging.get("nextPage"))).withRel("next"),
				linkTo(methodOn(ReroadApiController.class).retriveexpbusschedule(depTerminalId, arrTerminalId, depPlandTime, pageNo)).withSelfRel(),
				linkTo(methodOn(ReroadApiController.class).retriveexpbusschedule(depTerminalId, arrTerminalId, depPlandTime, paging.get("lastPage"))).withRel("last"));		
				
	}//retriveexpbusschedule() end
	
	
}//class end
