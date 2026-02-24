package com.example.demo.controller;

import com.example.demo.error.ErrorResponse;
import com.example.demo.entity.Mascota;
import com.example.demo.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@CrossOrigin(origins = "*")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    // CREATE
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> crear(
            @PathVariable Long usuarioId,
            @RequestBody Mascota mascota) {
        try {
            Mascota nueva = mascotaService.registrarMascota(usuarioId, mascota);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                400,
                "Bad Request",
                e.getMessage(),
                "/api/mascotas/usuario/" + usuarioId
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // READ - todas
    @GetMapping
    public ResponseEntity<List<Mascota>> listarTodas() {
        return ResponseEntity.ok(mascotaService.listarTodas());
    }

    // READ - por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return mascotaService.obtenerPorId(id)
                .map(mascota -> ResponseEntity.ok((Object) mascota))
                .orElseGet(() -> {
                    ErrorResponse error = new ErrorResponse(
                        404,
                        "Not Found",
                        "Mascota no encontrada con ID: " + id,
                        "/api/mascotas/" + id
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    // READ - por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        try {
            return ResponseEntity.ok(mascotaService.obtenerMascotasDeUsuario(usuarioId));
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                404,
                "Not Found",
                e.getMessage(),
                "/api/mascotas/usuario/" + usuarioId
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // SEARCH - por nombre
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String nombre) {
        try {
            return ResponseEntity.ok(mascotaService.buscarMascotasPorNombre(nombre));
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                400,
                "Bad Request",
                e.getMessage(),
                "/api/mascotas/buscar"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // SEARCH - por usuario + nombre
    @GetMapping("/usuario/{usuarioId}/buscar")
    public ResponseEntity<?> buscarMisMascotas(
            @PathVariable Long usuarioId,
            @RequestParam String nombre) {
        try {
            return ResponseEntity.ok(mascotaService.buscarDelUsuario(usuarioId, nombre));
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                400,
                "Bad Request",
                e.getMessage(),
                "/api/mascotas/usuario/" + usuarioId + "/buscar"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // UPDATE - con verificación de dueño
    @PutMapping("/{mascotaId}/usuario/{usuarioId}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long mascotaId,
            @PathVariable Long usuarioId,
            @RequestBody Mascota mascota) {
        try {
            Mascota actualizada = mascotaService.actualizarMascota(mascotaId, usuarioId, mascota);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            int statusCode = e.getMessage().contains("permiso") ? 403 : 404;
            String errorType = statusCode == 403 ? "Forbidden" : "Not Found";

            ErrorResponse error = new ErrorResponse(
                statusCode,
                errorType,
                e.getMessage(),
                "/api/mascotas/" + mascotaId + "/usuario/" + usuarioId
            );
            return ResponseEntity.status(statusCode).body(error);
        }
    }

    // DELETE - con verificación de dueño
    @DeleteMapping("/{mascotaId}/usuario/{usuarioId}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long mascotaId,
            @PathVariable Long usuarioId) {
        try {
            mascotaService.eliminarMascota(mascotaId, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            int statusCode = e.getMessage().contains("permiso") ? 403 : 404;
            String errorType = statusCode == 403 ? "Forbidden" : "Not Found";

            ErrorResponse error = new ErrorResponse(
                statusCode,
                errorType,
                e.getMessage(),
                "/api/mascotas/" + mascotaId + "/usuario/" + usuarioId
            );
            return ResponseEntity.status(statusCode).body(error);
        }
    }
}
