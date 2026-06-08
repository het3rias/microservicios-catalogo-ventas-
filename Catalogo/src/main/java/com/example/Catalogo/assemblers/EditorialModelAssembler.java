package com.example.Catalogo.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.example.Catalogo.controller.CatalogoControllerV2;
import com.example.Catalogo.model.Editorial;

@Component
public class EditorialModelAssembler implements RepresentationModelAssembler<Editorial, EntityModel<Editorial>> {

    @Override
    public EntityModel<Editorial> toModel(Editorial editorial) {
        return EntityModel.of(editorial,
                linkTo(methodOn(CatalogoControllerV2.class).getEditorialById(editorial.getId())).withSelfRel(),
                linkTo(methodOn(CatalogoControllerV2.class).getAllEditoriales()).withRel("editoriales"));
    }
}