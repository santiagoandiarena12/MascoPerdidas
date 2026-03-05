package com.example.demo.controller;

import com.example.demo.entity.Usuario;
import com.example.demo.entity.Rol;
import com.example.demo.repository.UsuarioRepo;
import com.example.demo.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        // Por ahora validación simple contra la DB (sin BCrypt aún)
        return usuarioRepo.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                // CAMBIO AQUÍ: Pasamos 'u' (el objeto Usuario) en lugar de 'email' (String)
                .map(u -> ResponseEntity.ok(Map.of("token", jwtService.generarToken(u))))
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario nuevoUsuario) {
        if (usuarioRepo.findByEmail(nuevoUsuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El email ya existe"));
        }

        // Si no se envía un rol en el JSON, se asigna ROLE_USER por defecto
        if (nuevoUsuario.getRol() == null) {
            nuevoUsuario.setRol(Rol.ROLE_USER);
        }

        Usuario guardado = usuarioRepo.save(nuevoUsuario);
        return ResponseEntity.ok(guardado);
    }
}