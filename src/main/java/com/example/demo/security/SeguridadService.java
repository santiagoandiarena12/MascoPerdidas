package com.example.demo.security;

import com.example.demo.repository.PublicacionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("seguridadService")
public class SeguridadService {
    @Autowired
    private PublicacionRepo publicacionRepo;

    // Devuelve true si el usuario logueado es el autor de la publicación
    public boolean esDuenioDePublicacion(Long idPublicacion, String emailLogueado) {
        return publicacionRepo.findById(idPublicacion)
                .map(p -> p.getAutor().getEmail().equals(emailLogueado))
                .orElse(false);
    }
}