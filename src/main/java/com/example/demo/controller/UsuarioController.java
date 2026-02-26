package com.example.demo.controller;

import com.example.demo.dto.ChangePasswordRequest;
import com.example.demo.entity.Usuario;
import com.example.demo.service.UsuarioService;
import com.example.demo.validation.CreateValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Validated
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // CREATE - Registro de nuevo usuario
    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody @Validated(CreateValidation.class) Usuario usuario) {
        Usuario nuevo = usuarioService.registrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // READ - Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // READ - Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable @Positive Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    // READ - Obtener usuario por email
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable @NotBlank @Email String email) {
        Usuario usuario = usuarioService.obtenerPorEmail(email);
        return ResponseEntity.ok(usuario);
    }

    // SEARCH - Buscar usuarios por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> buscarPorNombre(@RequestParam @NotBlank String nombre) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    // UPDATE - Actualizar datos del usuario (nombre, teléfono, coordenadas)
    @PutMapping("/{usuarioId}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable @Positive Long usuarioId,
            @RequestBody @Valid Usuario usuario) {
        Usuario actualizado = usuarioService.actualizar(usuarioId, usuario);
        return ResponseEntity.ok(actualizado);
    }

    // UPDATE - Cambiar contraseña (con verificación de contraseña actual)
    @PostMapping("/{usuarioId}/cambiar-password")
    public ResponseEntity<Void> cambiarPassword(
            @PathVariable @Positive Long usuarioId,
            @RequestBody @Valid ChangePasswordRequest request) {
        usuarioService.cambiarPassword(usuarioId, request.getPasswordActual(), request.getPasswordNueva());
        return ResponseEntity.noContent().build();
    }

    // DELETE - Eliminar usuario (requiere que no tenga mascotas)
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> eliminar(@PathVariable @Positive Long usuarioId) {
        usuarioService.eliminar(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
