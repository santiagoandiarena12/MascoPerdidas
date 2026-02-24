package com.example.demo.controller;

import com.example.demo.error.ErrorResponse;
import com.example.demo.entity.Usuario;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // CREATE - Registro de nuevo usuario
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                400,
                "Bad Request",
                e.getMessage(),
                "/api/usuarios/registro"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // READ - Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // READ - Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return usuarioService.obtenerPorId(id)
                    .map(usuario -> ResponseEntity.ok((Object) usuario))
                    .orElseGet(() -> {
                        ErrorResponse error = new ErrorResponse(
                            404,
                            "Not Found",
                            "Usuario no encontrado con ID: " + id,
                            "/api/usuarios/" + id
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                    });
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                400,
                "Bad Request",
                e.getMessage(),
                "/api/usuarios/" + id
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // READ - Obtener usuario por email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> obtenerPorEmail(@PathVariable String email) {
        try {
            Usuario usuario = usuarioService.obtenerPorEmail(email);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                404,
                "Not Found",
                e.getMessage(),
                "/api/usuarios/email/" + email
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // SEARCH - Buscar usuarios por nombre
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                400,
                "Bad Request",
                e.getMessage(),
                "/api/usuarios/buscar"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // UPDATE - Actualizar datos del usuario (nombre, teléfono, coordenadas)
    @PutMapping("/{usuarioId}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long usuarioId,
            @RequestBody Usuario usuario) {
        try {
            Usuario actualizado = usuarioService.actualizar(usuarioId, usuario);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            int statusCode = e.getMessage().contains("no encontrado") ? 404 : 400;
            String error = statusCode == 404 ? "Not Found" : "Bad Request";

            ErrorResponse errorResponse = new ErrorResponse(
                statusCode,
                error,
                e.getMessage(),
                "/api/usuarios/" + usuarioId
            );
            return ResponseEntity.status(statusCode).body(errorResponse);
        }
    }

    // UPDATE - Cambiar contraseña (con verificación de contraseña actual)
    @PostMapping("/{usuarioId}/cambiar-password")
    public ResponseEntity<?> cambiarPassword(
            @PathVariable Long usuarioId,
            @RequestBody Map<String, String> request) {
        try {
            String passwordActual = request.get("passwordActual");
            String passwordNueva = request.get("passwordNueva");

            if (passwordActual == null || passwordNueva == null) {
                ErrorResponse error = new ErrorResponse(
                    400,
                    "Bad Request",
                    "Los campos 'passwordActual' y 'passwordNueva' son obligatorios",
                    "/api/usuarios/" + usuarioId + "/cambiar-password"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            usuarioService.cambiarPassword(usuarioId, passwordActual, passwordNueva);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            int statusCode = 400;
            String errorType = "Bad Request";

            if (e.getMessage().contains("incorrecta")) {
                statusCode = 401;
                errorType = "Unauthorized";
            } else if (e.getMessage().contains("no encontrado")) {
                statusCode = 404;
                errorType = "Not Found";
            }

            ErrorResponse error = new ErrorResponse(
                statusCode,
                errorType,
                e.getMessage(),
                "/api/usuarios/" + usuarioId + "/cambiar-password"
            );
            return ResponseEntity.status(statusCode).body(error);
        }
    }

    // DELETE - Eliminar usuario (requiere que no tenga mascotas)
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> eliminar(@PathVariable Long usuarioId) {
        try {
            usuarioService.eliminar(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            int statusCode = 400;
            String errorType = "Bad Request";

            if (e.getMessage().contains("no encontrado")) {
                statusCode = 404;
                errorType = "Not Found";
            } else if (e.getMessage().contains("mascotas")) {
                statusCode = 409;
                errorType = "Conflict";
            }

            ErrorResponse error = new ErrorResponse(
                statusCode,
                errorType,
                e.getMessage(),
                "/api/usuarios/" + usuarioId
            );
            return ResponseEntity.status(statusCode).body(error);
        }
    }
}
