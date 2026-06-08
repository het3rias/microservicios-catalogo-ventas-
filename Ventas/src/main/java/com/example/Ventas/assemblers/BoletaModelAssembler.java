package com.example.Ventas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.example.Ventas.controller.VentasControllerV2;
import com.example.Ventas.model.Boleta;
 
@Component
public class BoletaModelAssembler implements RepresentationModelAssembler<Boleta, EntityModel<Boleta>> {
 
    @Override
    public EntityModel<Boleta> toModel(Boleta boleta) {
        return EntityModel.of(boleta,
                linkTo(methodOn(VentasControllerV2.class).getBoletaById(boleta.getId())).withSelfRel(),
                linkTo(methodOn(VentasControllerV2.class).getAllBoletas()).withRel("boletas"));
    }
}