package com.example.Ventas.assemblers;

import com.example.Ventas.controller.VentasControllerV2;
import com.example.Ventas.model.MetodoPago;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
 
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
 
@Component
public class MetodoPagoModelAssembler implements RepresentationModelAssembler<MetodoPago, EntityModel<MetodoPago>> {
 
    @Override
    public EntityModel<MetodoPago> toModel(MetodoPago metodoPago) {
        return EntityModel.of(metodoPago,
                linkTo(methodOn(VentasControllerV2.class).getMetodoPagoById(metodoPago.getId())).withSelfRel(),
                linkTo(methodOn(VentasControllerV2.class).getAllMetodoPagos()).withRel("metodoPagos"));
    }
}
 