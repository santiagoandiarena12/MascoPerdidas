package com.example.demo.service;

import com.example.demo.entity.Mascota;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.MascotaRepo;
import com.example.demo.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepo mascotaRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    // CREATE
    public Mascota registrarMascota(Long usuarioId, Mascota mascota) {
        Usuario duenio = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        validarDatosMascota(mascota);
        mascota.setDuenio(duenio);

        return mascotaRepo.save(mascota);
    }

    // READ - todas
    public List<Mascota> listarTodas() {
        return mascotaRepo.findAll();
    }

    // READ - por ID
    public Optional<Mascota> obtenerPorId(Long id) {
        return mascotaRepo.findById(id);
    }

    // READ - por usuario
    public List<Mascota> obtenerMascotasDeUsuario(Long usuarioId) {
        if (!usuarioRepo.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }
        return mascotaRepo.findByDuenioId(usuarioId);
    }

    // READ - buscar por nombre
    public List<Mascota> buscarMascotasPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre de búsqueda no puede estar vacío");
        }
        return mascotaRepo.findByNombreContainingIgnoreCase(nombre);
    }

    // READ - buscar por usuario + nombre
    public List<Mascota> buscarDelUsuario(Long usuarioId, String nombre) {
        if (!usuarioRepo.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre de búsqueda no puede estar vacío");
        }
        return mascotaRepo.findByDuenioIdAndNombreContainingIgnoreCase(usuarioId, nombre);
    }

    // UPDATE - con verificación de propietario
    public Mascota actualizarMascota(Long mascotaId, Long usuarioId, Mascota mascotaActualizada) {
        Mascota existente = mascotaRepo.findById(mascotaId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + mascotaId));

        if (!esDuenio(existente, usuarioId)) {
            throw new RuntimeException("No tienes permiso para actualizar esta mascota");
        }

        validarDatosMascota(mascotaActualizada);

        existente.setNombre(mascotaActualizada.getNombre());
        existente.setEspecie(mascotaActualizada.getEspecie());
        existente.setRaza(mascotaActualizada.getRaza());
        if (mascotaActualizada.getFotoUrl() != null) {
            existente.setFotoUrl(mascotaActualizada.getFotoUrl());
        }

        return mascotaRepo.save(existente);
    }

    // DELETE - con verificación de propietario
    public void eliminarMascota(Long mascotaId, Long usuarioId) {
        Mascota existente = mascotaRepo.findById(mascotaId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + mascotaId));

        if (!esDuenio(existente, usuarioId)) {
            throw new RuntimeException("No tienes permiso para eliminar esta mascota");
        }

        mascotaRepo.deleteById(mascotaId);
    }

    // Helpers
    private boolean esDuenio(Mascota mascota, Long usuarioId) {
        return mascota.getDuenio() != null && mascota.getDuenio().getId().equals(usuarioId);
    }

    private void validarDatosMascota(Mascota mascota) {
        if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la mascota es obligatorio");
        }
        if (mascota.getEspecie() == null || mascota.getEspecie().trim().isEmpty()) {
            throw new RuntimeException("La especie de la mascota es obligatoria");
        }
        if (mascota.getNombre().length() > 50) {
            throw new RuntimeException("El nombre de la mascota no puede exceder 50 caracteres");
        }
    }
}
