package com.example.demo.controller;

import com.example.demo.error.ErrorResponse;
import com.example.demo.dto.ReporteMascotasCercanasDTO;
import com.example.demo.entity.Publicacion;
import com.example.demo.entity.TipoPublicacion;
import com.example.demo.service.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@CrossOrigin(origins = "*")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    // CREATE
    @PostMapping
    public ResponseEntity<?> crearPublicacion(
            @RequestParam Long usuarioId,
            @RequestParam Long mascotaId,
            @RequestBody Publicacion publicacion) {
        try {
            Publicacion nueva = publicacionService.crearPublicacion(usuarioId, mascotaId, publicacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                400,
                "Bad Request",
                e.getMessage(),
                "/api/publicaciones"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // READ - Obtener todas
    @GetMapping
    public ResponseEntity<List<Publicacion>> listarTodas() {
        List<Publicacion> publicaciones = publicacionService.listarTodas();
        return ResponseEntity.ok(publicaciones);
    }

    // READ - Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return publicacionService.obtenerPorId(id)
                .map(publicacion -> ResponseEntity.ok((Object) publicacion))
                .orElseGet(() -> {
                    ErrorResponse error = new ErrorResponse(
                        404,
                        "Not Found",
                        "Publicación no encontrada con ID: " + id,
                        "/api/publicaciones/" + id
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    // READ - Obtener por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> obtenerPorTipo(@PathVariable TipoPublicacion tipo) {
        try {
            List<Publicacion> publicaciones = publicacionService.obtenerPorTipo(tipo);
            return ResponseEntity.ok(publicaciones);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                400,
                "Bad Request",
                e.getMessage(),
                "/api/publicaciones/tipo/" + tipo
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // READ - Obtener por autor
    @GetMapping("/autor/{usuarioId}")
    public ResponseEntity<?> obtenerPorAutor(@PathVariable Long usuarioId) {
        try {
            List<Publicacion> publicaciones = publicacionService.obtenerPorAutor(usuarioId);
            return ResponseEntity.ok(publicaciones);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                404,
                "Not Found",
                e.getMessage(),
                "/api/publicaciones/autor/" + usuarioId
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPublicacion(
            @PathVariable Long id,
            @RequestBody Publicacion publicacion) {
        try {
            Publicacion actualizada = publicacionService.actualizarPublicacion(id, publicacion);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                404,
                "Not Found",
                e.getMessage(),
                "/api/publicaciones/" + id
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPublicacion(@PathVariable Long id) {
        try {
            publicacionService.eliminarPublicacion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                404,
                "Not Found",
                e.getMessage(),
                "/api/publicaciones/" + id
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Búsqueda de mascotas cercanas
    @GetMapping("/cercanas")
    public ResponseEntity<?> buscarCercanas(
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam Double radioKm) {
        try {
            List<ReporteMascotasCercanasDTO> cercanas = publicacionService.buscarCercanas(latitud, longitud, radioKm);
            return ResponseEntity.ok(cercanas);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                400,
                "Bad Request",
                e.getMessage(),
                "/api/publicaciones/cercanas"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
