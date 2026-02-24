package com.example.demo.controller;

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
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    // CREATE
    @PostMapping
    public ResponseEntity<Publicacion> crearPublicacion(
            @RequestParam Long usuarioId,
            @RequestParam Long mascotaId,
            @RequestBody Publicacion publicacion) {
        Publicacion nueva = publicacionService.crearPublicacion(usuarioId, mascotaId, publicacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // READ - Obtener todas
    @GetMapping
    public ResponseEntity<List<Publicacion>> listarTodas() {
        List<Publicacion> publicaciones = publicacionService.listarTodas();
        return ResponseEntity.ok(publicaciones);
    }

    // READ - Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> obtenerPorId(@PathVariable Long id) {
        return publicacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // READ - Obtener por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Publicacion>> obtenerPorTipo(@PathVariable TipoPublicacion tipo) {
        List<Publicacion> publicaciones = publicacionService.obtenerPorTipo(tipo);
        return ResponseEntity.ok(publicaciones);
    }

    // READ - Obtener por autor
    @GetMapping("/autor/{usuarioId}")
    public ResponseEntity<List<Publicacion>> obtenerPorAutor(@PathVariable Long usuarioId) {
        List<Publicacion> publicaciones = publicacionService.obtenerPorAutor(usuarioId);
        return ResponseEntity.ok(publicaciones);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Publicacion> actualizarPublicacion(
            @PathVariable Long id,
            @RequestBody Publicacion publicacion) {
        try {
            Publicacion actualizada = publicacionService.actualizarPublicacion(id, publicacion);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPublicacion(@PathVariable Long id) {
        try {
            publicacionService.eliminarPublicacion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Búsqueda de mascotas cercanas
    @GetMapping("/cercanas")
    public ResponseEntity<List<ReporteMascotasCercanasDTO>> buscarCercanas(
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam Double radioKm) {
        List<ReporteMascotasCercanasDTO> cercanas = publicacionService.buscarCercanas(latitud, longitud, radioKm);
        return ResponseEntity.ok(cercanas);
    }
}
