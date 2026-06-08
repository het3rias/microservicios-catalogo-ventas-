package com.example.Ventas.assemblers;

import com.example.Ventas.controller.VentasControllerV2;
import com.example.Ventas.model.DetalleBoleta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
 
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
 
@Component
public class DetalleBoletaModelAssembler implements RepresentationModelAssembler<DetalleBoleta, EntityModel<DetalleBoleta>> {
 
    @Override
    public EntityModel<DetalleBoleta> toModel(DetalleBoleta detalleBoleta) {
        return EntityModel.of(detalleBoleta,
                linkTo(methodOn(VentasControllerV2.class).getDetalleBoletaById(detalleBoleta.getId())).withSelfRel(),
                linkTo(methodOn(VentasControllerV2.class).getAllDetalleBoletas()).withRel("detalleBoletas"));
    }
}
 