package com.example.Ventas.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.example.Ventas.controller.VentasControllerV2;
import com.example.Ventas.model.Pago;
 
@Component
public class PagoModelAssembler implements RepresentationModelAssembler<Pago, EntityModel<Pago>> {
 
    @Override
    public EntityModel<Pago> toModel(Pago pago) {
        return EntityModel.of(pago,
                linkTo(methodOn(VentasControllerV2.class).getPagoById(pago.getId())).withSelfRel(),
                linkTo(methodOn(VentasControllerV2.class).getAllPagos()).withRel("pagos"));
    }
}
 
