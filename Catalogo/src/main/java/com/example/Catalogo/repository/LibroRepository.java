package com.example.Catalogo.repository;

import com.example.Catalogo.model.Libro;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {


    // Por categoría
    List<Libro> findByCategoria_Id(Integer idCategoria);

    // Por título
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    // Por autor (nombre o apellido)
    List<Libro> findByAutor_NombreContainingIgnoreCaseOrAutor_ApellidoContainingIgnoreCase(String nombre, String apellido);

    // Por editorial
    List<Libro> findByEditorial_NombreContainingIgnoreCase(String editorial);

    // Por disponibilidad
    List<Libro> findByDisponible(Boolean disponible);

    // Por rango de precio
    List<Libro> findByPrecioBetween(Double min, Double max);

    // Ordenados por precio
    List<Libro> findAllByOrderByPrecioAsc();
    List<Libro> findAllByOrderByPrecioDesc();

    // Por rango de fecha de publicación
    List<Libro> findByAnioPublicacionBetween(Date desde, Date hasta);

    // Búsqueda combinada
    @Query("SELECT l FROM Libro l WHERE " +
            "(:titulo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) AND " +
            "(:autor IS NULL OR LOWER(l.autor.nombre) LIKE LOWER(CONCAT('%', :autor, '%')) OR LOWER(l.autor.apellido) LIKE LOWER(CONCAT('%', :autor, '%'))) AND " +
            "(:editorial IS NULL OR LOWER(l.editorial.nombre) LIKE LOWER(CONCAT('%', :editorial, '%'))) AND " +
            "(:idCategoria IS NULL OR l.categoria.id = :idCategoria) AND " +
            "(:disponible IS NULL OR l.disponible = :disponible)")
    List<Libro> buscarConFiltros(
            @Param("titulo") String titulo,
            @Param("autor") String autor,
            @Param("editorial") String editorial,
            @Param("idCategoria") Integer idCategoria,
            @Param("disponible") Boolean disponible
    );



}
