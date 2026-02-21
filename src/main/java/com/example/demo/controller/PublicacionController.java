package com.example.demo.controller;

import com.example.demo.dto.ReporteMascotasCercanasDTO;
import com.example.demo.entity.Publicacion;
import com.example.demo.service.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@CrossOrigin(origins = "*") // Permite que Angular se conecte sin problemas de CORS
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    @GetMapping
    public List<Publicacion> obtenerTodas() {
        return publicacionService.listarTodas();
    }

    @PostMapping("/usuario/{uId}/mascota/{mId}")
    public Publicacion publicar(
            @PathVariable Long uId,
            @PathVariable Long mId,
            @RequestBody Publicacion publicacion) {
        return publicacionService.crearPublicacion(uId, mId, publicacion);
    }

    @GetMapping("/cercanas")
    public List<ReporteMascotasCercanasDTO> obtenerCercanas(@RequestParam Double lat, @RequestParam Double lon, @RequestParam(defaultValue = "2.0") Double radio){
        return publicacionService.buscarCercanas(lat, lon, radio);
    }
}
