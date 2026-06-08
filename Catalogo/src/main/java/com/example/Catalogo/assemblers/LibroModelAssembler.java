package com.example.Catalogo.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.example.Catalogo.controller.CatalogoControllerV2;
import com.example.Catalogo.model.Libro;

@Component
public class LibroModelAssembler implements RepresentationModelAssembler<Libro, EntityModel<Libro>> {

    @Override
    public EntityModel<Libro> toModel(Libro libro) {
        return EntityModel.of(libro,
                linkTo(methodOn(CatalogoControllerV2.class).getLibroById(libro.getId())).withSelfRel(),
                linkTo(methodOn(CatalogoControllerV2.class).getAllLibros()).withRel("libros"));
    }
}