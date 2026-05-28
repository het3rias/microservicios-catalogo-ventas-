package com.example.Catalogo.service;

import com.example.Catalogo.model.Editorial;
import com.example.Catalogo.repository.EditorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EditorialService {

    @Autowired
    private EditorialRepository editorialRepository;

    public Editorial  guardarEditorial(Editorial editorial) {return editorialRepository.save(editorial);}

    public List<Editorial> findAll() {return editorialRepository.findAll();}

    public Optional<Editorial> findById(Integer id) {return editorialRepository.findById(id);}

}
