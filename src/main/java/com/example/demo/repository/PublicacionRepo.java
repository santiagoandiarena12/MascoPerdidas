package com.example.demo.repository;

import com.example.demo.entity.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("PublicacionRepo")
public interface PublicacionRepo extends JpaRepository<Publicacion, Long> {

    List<Publicacion> findByTipo(String tipo);

}
