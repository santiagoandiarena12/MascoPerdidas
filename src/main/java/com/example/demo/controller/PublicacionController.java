package com.example.demo.controller;

import com.example.demo.dto.ReporteMascotasCercanasDTO;
import com.example.demo.entity.Publicacion;
import com.example.demo.entity.TipoPublicacion;
import com.example.demo.service.PublicacionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@CrossOrigin(origins = "*")
@Validated
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    // CREATE
    @PostMapping
    public ResponseEntity<Publicacion> crearPublicacion(
            @RequestParam @Positive Long usuarioId,
            @RequestParam @Positive Long mascotaId,
            @RequestBody @Valid Publicacion publicacion) {
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
    public ResponseEntity<Publicacion> obtenerPorId(@PathVariable @Positive Long id) {
        return publicacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + id));
    }

    // READ - Obtener por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Publicacion>> obtenerPorTipo(@PathVariable TipoPublicacion tipo) {
        List<Publicacion> publicaciones = publicacionService.obtenerPorTipo(tipo);
        return ResponseEntity.ok(publicaciones);
    }

    // READ - Obtener por autor
    @GetMapping("/autor/{usuarioId}")
    public ResponseEntity<List<Publicacion>> obtenerPorAutor(@PathVariable @Positive Long usuarioId) {
        List<Publicacion> publicaciones = publicacionService.obtenerPorAutor(usuarioId);
        return ResponseEntity.ok(publicaciones);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Publicacion> actualizarPublicacion(
            @PathVariable @Positive Long id,
            @RequestBody @Valid Publicacion publicacion) {
        Publicacion actualizada = publicacionService.actualizarPublicacion(id, publicacion);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPublicacion(@PathVariable @Positive Long id) {
        publicacionService.eliminarPublicacion(id);
        return ResponseEntity.noContent().build();
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

    // Busqueda de mascotas en Tandil
    @GetMapping("/mapa")
    public ResponseEntity<List<ReporteMascotasCercanasDTO>> ObtenerMascotasEnMapa() {
        double latTandil = -37.3217; //fijas de la ciudad de Tandil
        double lonTandil = -59.1331;
        double radioMapa = 10.0;

        List<ReporteMascotasCercanasDTO> cercanas = publicacionService.buscarCercanas(latTandil, lonTandil, radioMapa);
        return ResponseEntity.ok(cercanas);
    }

}
