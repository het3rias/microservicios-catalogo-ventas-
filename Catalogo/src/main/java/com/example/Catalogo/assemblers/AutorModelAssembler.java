package com.example.Catalogo.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.example.Catalogo.controller.CatalogoControllerV2;
import com.example.Catalogo.model.Autor;

@Component
public class AutorModelAssembler implements RepresentationModelAssembler<Autor, EntityModel<Autor>> {

    @Override
    public EntityModel<Autor> toModel(Autor autor) {
        return EntityModel.of(autor,
                linkTo(methodOn(CatalogoControllerV2.class).getAutorById(autor.getId())).withSelfRel(),
                linkTo(methodOn(CatalogoControllerV2.class).getAllAutores()).withRel("autores"));
    }
}