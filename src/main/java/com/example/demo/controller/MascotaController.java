package com.example.demo.controller;

import com.example.demo.entity.Mascota;
import com.example.demo.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@CrossOrigin(origins = "*")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @PostMapping("/usuario/{usuarioId}")
    public Mascota guardar(@PathVariable Long usuarioId, @RequestBody Mascota mascota) {
        return mascotaService.registrarMascota(usuarioId, mascota);
    }

    //buscador general /api/mascotas/buscar?nombre=Firulais
    @GetMapping("/buscar")
    public List<Mascota> buscar(@RequestParam String nombre) {
        return mascotaService.buscarMascotasPorNombre(nombre);
    }

    // Buscador mis mascotas: /api/mascotas/usuario/1/buscar?nombre=firu
    @GetMapping("/usuario/{uId}/buscar")
    public List<Mascota> buscarMisMascotas(@PathVariable Long uId, @RequestParam String nombre) {
        return mascotaService.buscarDelUsuario(uId, nombre);
    }
}
