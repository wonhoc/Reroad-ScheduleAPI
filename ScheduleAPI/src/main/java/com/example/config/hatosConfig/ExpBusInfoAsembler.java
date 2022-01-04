package com.example.config.hatosConfig;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.controller.ReroadApiController;
import com.example.vo.expBusVo.ExpBusTmlInfoVo;

@Component
public class ExpBusInfoAsembler implements RepresentationModelAssembler<ExpBusTmlInfoVo, EntityModel<ExpBusTmlInfoVo>>  {
	
	@Override
	public EntityModel<ExpBusTmlInfoVo> toModel(ExpBusTmlInfoVo expBusTmlInfo) {
		
		return EntityModel.of(expBusTmlInfo,
				linkTo(methodOn(ReroadApiController.class).expbustmlinfo()).withRel("all-city-expbus-terminal-list"),
				linkTo(methodOn(ReroadApiController.class).expbustmlinfo()).withSelfRel());
	}//toModel() end
	
	@Override
	public CollectionModel<EntityModel<ExpBusTmlInfoVo>> toCollectionModel(
			Iterable<? extends ExpBusTmlInfoVo> expBusTmlInfo) {
	
		return RepresentationModelAssembler.super.toCollectionModel(expBusTmlInfo)
				.add(linkTo(methodOn(ReroadApiController.class).citytrainstinfo()).withRel("all-city-expbus-terminal-list"),
					linkTo(methodOn(ReroadApiController.class).citytrainstinfo()).withSelfRel());
	}//toCollectionModel() end
	
	
	
}//class end
