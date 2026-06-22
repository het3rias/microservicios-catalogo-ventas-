package com.example.Catalogo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.Catalogo.model.Autor;
import com.example.Catalogo.model.Categoria;
import com.example.Catalogo.model.Editorial;
import com.example.Catalogo.model.Libro;
import com.example.Catalogo.repository.AutorRepository;
import com.example.Catalogo.repository.CategoriaRepository;
import com.example.Catalogo.repository.EditorialRepository;
import com.example.Catalogo.repository.LibroRepository;

import net.datafaker.Faker;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private EditorialRepository editorialRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Override
    public void run(String... args) throws Exception {

        // Evita duplicar datos si ya existen
        if (libroRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        Random random = new Random();

        // Generar autores
        for (int i = 0; i < 10; i++) {
            Autor autor = new Autor();
            autor.setNombre(faker.name().firstName());
            autor.setApellido(faker.name().lastName());
            autorRepository.save(autor);
        }
        List<Autor> autores = autorRepository.findAll();

        // Generar editoriales
        for (int i = 0; i < 5; i++) {
            Editorial editorial = new Editorial();
            editorial.setNombre(faker.book().publisher());
            editorialRepository.save(editorial);
        }
        List<Editorial> editoriales = editorialRepository.findAll();

        // Generar categorías
        for (int i = 0; i < 6; i++) {
            Categoria categoria = new Categoria();
            categoria.setNombre(faker.book().genre());
            categoriaRepository.save(categoria);
        }
        List<Categoria> categorias = categoriaRepository.findAll();

        // Generar libros
        for (int i = 0; i < 40; i++) {
            Libro libro = new Libro();
            libro.setTitulo(faker.book().title());
            libro.setAnioPublicacion(fechaAleatoria(random));
            libro.setDisponible(faker.bool().bool());
            libro.setPrecio(redondear(faker.number().randomDouble(2, 5000, 35000)));
            libro.setAutor(autores.get(random.nextInt(autores.size())));
            libro.setEditorial(editoriales.get(random.nextInt(editoriales.size())));
            libro.setCategoria(categorias.get(random.nextInt(categorias.size())));
            libroRepository.save(libro);
        }
    }

    private Date fechaAleatoria(Random random) {
        Calendar calendar = Calendar.getInstance();
        int anio = 1990 + random.nextInt(36); // entre 1990 y 2025
        int dia = 1 + random.nextInt(28);
        int mes = random.nextInt(12);
        calendar.set(anio, mes, dia);
        return calendar.getTime();
    }

    private Double redondear(Double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}