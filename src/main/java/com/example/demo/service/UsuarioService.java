package com.example.demo.service;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepo userRepo;

    public Usuario crearUsuario(Usuario usuario){
        return userRepo.save(usuario);
    }

    public Usuario obtenerPorId(Long id){
        return userRepo.findById(id).orElse(null);
    }

    public Usuario buscarPorEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con el email: " + email));
    }
}
