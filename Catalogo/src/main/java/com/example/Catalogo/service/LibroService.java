package com.example.Catalogo.service;

import com.example.Catalogo.exception.BadRequestException;
import com.example.Catalogo.exception.ResourceNotFoundException;
import com.example.Catalogo.model.Libro;
import com.example.Catalogo.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public Libro guardarLibro(Libro libro) {return libroRepository.save(libro); }

    public List<Libro> findAll() {return libroRepository.findAll();}

    public Libro findById(Integer id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro con id " + id + " no encontrado"));
    }


    public List<Libro> filtrarPorCategoria(Integer idCategoria) {
        List<Libro> resultado = libroRepository.findByCategoria_Id(idCategoria);
        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No hay libros en la categoría " + idCategoria);
        return resultado;
    }

    public List<Libro> filtrarPorTitulo(String titulo) {
        if (titulo == null || titulo.isBlank())
            throw new BadRequestException("El título de búsqueda no puede estar vacío");
        List<Libro> resultado = libroRepository.findByTituloContainingIgnoreCase(titulo);
        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No se encontraron libros con el título: " + titulo);
        return resultado;
    }

    public List<Libro> filtrarPorAutor(String autor) {
        if (autor == null || autor.isBlank())
            throw new BadRequestException("El nombre del autor no puede estar vacío");
        List<Libro> resultado = libroRepository
                .findByAutor_NombreContainingIgnoreCaseOrAutor_ApellidoContainingIgnoreCase(autor, autor);
        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No se encontraron libros del autor: " + autor);
        return resultado;
    }

    public List<Libro> filtrarPorEditorial(String editorial) {
        return libroRepository.findByEditorial_NombreContainingIgnoreCase(editorial);
    }

    public List<Libro> filtrarPorDisponibilidad(Boolean disponible) {
        return libroRepository.findByDisponible(disponible);
    }

    public List<Libro> filtrarPorRangoPrecio(Double min, Double max) {
        if (min == null || max == null)
            throw new BadRequestException("Debe indicar precio mínimo y máximo");
        if (min < 0 || max < 0)
            throw new BadRequestException("Los precios no pueden ser negativos");
        if (min > max)
            throw new BadRequestException("El precio mínimo no puede ser mayor al máximo");
        List<Libro> resultado = libroRepository.findByPrecioBetween(min, max);
        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No hay libros en el rango de precio " + min + " - " + max);
        return resultado;
    }

    public List<Libro> librosMasBaratos() { return libroRepository.findAllByOrderByPrecioAsc(); }
    public List<Libro> librosMasCaros()   { return libroRepository.findAllByOrderByPrecioDesc(); }

    public List<Libro> filtrarPorRangoFecha(Date desde, Date hasta) {
        if (desde.after(hasta))
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha fin");
        List<Libro> resultado = libroRepository.findByAnioPublicacionBetween(desde, hasta);
        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No hay libros en ese rango de fechas");
        return resultado;
    }

    public List<Libro> buscarConFiltros(String titulo, String autor, String editorial,
                                        Integer idCategoria, Boolean disponible) {
        List<Libro> resultado = libroRepository.buscarConFiltros(titulo, autor, editorial, idCategoria, disponible);
        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No se encontraron libros con esos filtros");
        return resultado;
    }

}
