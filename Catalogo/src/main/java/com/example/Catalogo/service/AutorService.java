package com.example.Catalogo.service;

import com.example.Catalogo.model.Autor;
import com.example.Catalogo.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> findAll() {return autorRepository.findAll();}

    public Autor guardarAutor(Autor autor) {return autorRepository.save(autor);}

    public Optional<Autor> findById(Integer id) {return autorRepository.findById(id);}

}
