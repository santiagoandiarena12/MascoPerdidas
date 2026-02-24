package com.example.demo.repository;

import com.example.demo.entity.Publicacion;
import com.example.demo.entity.TipoPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepo extends JpaRepository<Publicacion, Long> {
    List<Publicacion> findByTipo(TipoPublicacion tipo);
    List<Publicacion> findByAutorId(Long usuarioId);
}
