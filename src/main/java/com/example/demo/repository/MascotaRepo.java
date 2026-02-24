package com.example.demo.repository;

import com.example.demo.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepo extends JpaRepository<Mascota, Long> {

    List<Mascota> findByNombreContainingIgnoreCase(String nombre);

    List<Mascota> findByDuenioIdAndNombreContainingIgnoreCase(Long usuarioId, String nombre);

    List<Mascota> findByDuenioId(Long usuarioId);
}
