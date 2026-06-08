package com.example.Catalogo.assemblers;
 
import com.example.Catalogo.controller.CatalogoControllerV2;
import com.example.Catalogo.model.Categoria;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
 
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
 
@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<Categoria, EntityModel<Categoria>> {
 
    @Override
    public EntityModel<Categoria> toModel(Categoria categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CatalogoControllerV2.class).getCategoriaById(categoria.getId())).withSelfRel(),
                linkTo(methodOn(CatalogoControllerV2.class).getAllCategorias()).withRel("categorias"));
    }
}
 