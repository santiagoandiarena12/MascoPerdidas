package com.example.demo.service;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepo usuarioRepo;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // CREATE
    public Usuario registrar(Usuario usuario) {
        validarDatosRegistro(usuario);

        if (usuarioRepo.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado: " + usuario.getEmail());
        }

        return usuarioRepo.save(usuario);
    }

    // READ - obtener todos
    public List<Usuario> listarTodos() {
        return usuarioRepo.findAll();
    }

    // READ - obtener por ID
    public Optional<Usuario> obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("ID de usuario inválido");
        }
        return usuarioRepo.findById(id);
    }

    // READ - obtener por email
    public Usuario obtenerPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("El email no puede estar vacío");
        }
        return usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    // READ - buscar por nombre
    public List<Usuario> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre de búsqueda no puede estar vacío");
        }
        return usuarioRepo.findByNombreContainingIgnoreCase(nombre);
    }

    // UPDATE - con verificación de identidad
    public Usuario actualizar(Long usuarioId, Usuario usuarioActualizado) {
        Usuario existente = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        validarDatosActualizacion(usuarioActualizado, existente.getId());

        // Actualizar campos permitidos
        if (usuarioActualizado.getNombre() != null && !usuarioActualizado.getNombre().trim().isEmpty()) {
            existente.setNombre(usuarioActualizado.getNombre());
        }

        if (usuarioActualizado.getTelefono() != null) {
            existente.setTelefono(usuarioActualizado.getTelefono());
        }

        if (usuarioActualizado.getLatitudCasa() != null) {
            existente.setLatitudCasa(usuarioActualizado.getLatitudCasa());
        }

        if (usuarioActualizado.getLongitudCasa() != null) {
            existente.setLongitudCasa(usuarioActualizado.getLongitudCasa());
        }

        return usuarioRepo.save(existente);
    }

    // UPDATE - cambiar contraseña (requiere verificación)
    public Usuario cambiarPassword(Long usuarioId, String passwordActual, String passwordNueva) {
        Usuario existente = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        // Verificar contraseña actual (aquí deberías usar BCrypt en producción)
        if (!existente.getPassword().equals(passwordActual)) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        validarPassword(passwordNueva);

        existente.setPassword(passwordNueva);
        return usuarioRepo.save(existente);
    }

    // DELETE - con verificación de identidad
    public void eliminar(Long usuarioId) {
        Usuario existente = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        // Validación: no eliminar si tiene mascotas activas
        if (existente.getMascotas() != null && !existente.getMascotas().isEmpty()) {
            throw new RuntimeException("No puedes eliminar tu cuenta mientras tengas mascotas registradas");
        }

        usuarioRepo.deleteById(usuarioId);
    }

    // Helpers
    private void validarDatosRegistro(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del usuario es obligatorio");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email del usuario es obligatorio");
        }

        if (!EMAIL_PATTERN.matcher(usuario.getEmail()).matches()) {
            throw new RuntimeException("El formato del email es inválido");
        }

        validarPassword(usuario.getPassword());

        if (usuario.getNombre().length() > 100) {
            throw new RuntimeException("El nombre no puede exceder 100 caracteres");
        }

        if (usuario.getEmail().length() > 100) {
            throw new RuntimeException("El email no puede exceder 100 caracteres");
        }
    }

    private void validarDatosActualizacion(Usuario usuario, Long usuarioId) {
        // Verificar que no intente cambiar email
        Usuario existente = usuarioRepo.findById(usuarioId).orElse(null);
        if (existente != null && usuario.getEmail() != null &&
                !usuario.getEmail().equals(existente.getEmail())) {
            throw new RuntimeException("No puedes cambiar tu email");
        }

        // Verificar que no intente cambiar password por aquí
        if (usuario.getPassword() != null &&
                !usuario.getPassword().equals(existente.getPassword())) {
            throw new RuntimeException("Usa el endpoint de cambiar contraseña");
        }

        if (usuario.getNombre() != null && usuario.getNombre().length() > 100) {
            throw new RuntimeException("El nombre no puede exceder 100 caracteres");
        }
    }

    private void validarPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        if (password.length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        if (password.length() > 100) {
            throw new RuntimeException("La contraseña no puede exceder 100 caracteres");
        }
    }
}
