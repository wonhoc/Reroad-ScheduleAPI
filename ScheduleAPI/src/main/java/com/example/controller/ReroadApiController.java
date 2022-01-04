package com.example.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
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
import com.example.vo.CityInfoVo;
import com.example.vo.expBusVo.ExpBusScVo;
import com.example.vo.expBusVo.ExpBusSetInfoVo;
import com.example.vo.expBusVo.ExpBusTmlInfoVo;
import com.example.vo.trainVo.CityTrainStInfoVo;
import com.example.vo.trainVo.TrainScVo;
import com.example.vo.trainVo.TrainSetInfoVo;
import com.example.vo.trainVo.TrainStInfoVo;

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
	
	@Autowired
	private CityInfoDao cityInfoDao;
	
	@GetMapping("/citytrainstinfo")
	public CollectionModel<EntityModel<CityTrainStInfoVo>> citytrainstinfo() {

		ArrayList<CityTrainStInfoVo> CityTrainStInfoList = this.trainService.getCityTrainStInfo();
		
		return this.trainInfoAssembler.toCollectionModel(CityTrainStInfoList);
		
	}//home() end
	
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
	
	@GetMapping("/home")
	public String home() throws Exception {
		
		
		for(CityInfoVo vo :this.trainService.getCityCode()) {
			
			System.out.println(vo.getCityCode());
			
		ArrayList<TrainStInfoVo> stinfos =	this.trainService.getTrainStInfo(vo.getCityCode());
			
			for(TrainStInfoVo stinfo : stinfos) {
				
				this.cityInfoDao.insertTrainStInfo(stinfo);
				
			}//for end
		
			
		}
		
		
		
		
		return "hello";
	}
	
	
	
	
}//class end
