package com.example.demo.controller;

import com.example.demo.entity.Usuario;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @GetMapping("/buscar-por-email")
    public Usuario obtenerPorEmail(@RequestParam String email) {
        return usuarioService.buscarPorEmail(email);
    }
}
