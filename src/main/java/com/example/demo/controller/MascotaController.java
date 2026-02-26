package com.example.demo.controller;

import com.example.demo.entity.Mascota;
import com.example.demo.service.MascotaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@CrossOrigin(origins = "*")
@Validated
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    // CREATE
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Mascota> crear(
            @PathVariable @Positive Long usuarioId,
            @RequestBody @Valid Mascota mascota) {
        Mascota nueva = mascotaService.registrarMascota(usuarioId, mascota);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // READ - todas
    @GetMapping
    public ResponseEntity<List<Mascota>> listarTodas() {
        return ResponseEntity.ok(mascotaService.listarTodas());
    }

    // READ - por ID
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtenerPorId(@PathVariable @Positive Long id) {
        return mascotaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));
    }

    // READ - por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Mascota>> listarPorUsuario(@PathVariable @Positive Long usuarioId) {
        return ResponseEntity.ok(mascotaService.obtenerMascotasDeUsuario(usuarioId));
    }

    // SEARCH - por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Mascota>> buscar(@RequestParam @NotBlank String nombre) {
        return ResponseEntity.ok(mascotaService.buscarMascotasPorNombre(nombre));
    }

    // SEARCH - por usuario + nombre
    @GetMapping("/usuario/{usuarioId}/buscar")
    public ResponseEntity<List<Mascota>> buscarMisMascotas(
            @PathVariable @Positive Long usuarioId,
            @RequestParam @NotBlank String nombre) {
        return ResponseEntity.ok(mascotaService.buscarDelUsuario(usuarioId, nombre));
    }

    // UPDATE - con verificación de dueño
    @PutMapping("/{mascotaId}/usuario/{usuarioId}")
    public ResponseEntity<Mascota> actualizar(
            @PathVariable @Positive Long mascotaId,
            @PathVariable @Positive Long usuarioId,
            @RequestBody @Valid Mascota mascota) {
        Mascota actualizada = mascotaService.actualizarMascota(mascotaId, usuarioId, mascota);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE - con verificación de dueño
    @DeleteMapping("/{mascotaId}/usuario/{usuarioId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive Long mascotaId,
            @PathVariable @Positive Long usuarioId) {
        mascotaService.eliminarMascota(mascotaId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
