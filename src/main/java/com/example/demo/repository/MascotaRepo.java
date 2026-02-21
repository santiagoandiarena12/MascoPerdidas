package com.example.demo.repository;

import com.example.demo.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("MascotaRepo")
public interface MascotaRepo extends JpaRepository<Mascota, Long> {

    List<Mascota> findByNombreContainingIgnoreCase(String nombre);

    // Para que el usuario busque entre sus propios animales
    List<Mascota> findByDuenioIdAndNombreContainingIgnoreCase(Long usuarioId, String nombre);
}
