
package com.example.config.hatosConfig;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;

import com.example.controller.ReroadApiController;
import com.example.vo.trainVo.CityTrainStInfoVo;

//HATEOAS 설정

@Component
public class TrainInfoAssembler implements RepresentationModelAssembler<CityTrainStInfoVo, EntityModel<CityTrainStInfoVo>>{

	@Override
	public EntityModel<CityTrainStInfoVo> toModel(CityTrainStInfoVo cityTrainStInfo) {
		
		return EntityModel.of(cityTrainStInfo);

	}//toModel() end
	
	@Override
	public CollectionModel<EntityModel<CityTrainStInfoVo>> toCollectionModel(
			Iterable<? extends CityTrainStInfoVo> CityTrainStInfoList) {
	
		return RepresentationModelAssembler.super.toCollectionModel(CityTrainStInfoList)
				.add(linkTo(methodOn(ReroadApiController.class).citytrainstinfo()).withSelfRel());
	}//toCollectionModel() end
	
	
	
}//class end