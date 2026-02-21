package com.example.demo.service;

import com.example.demo.entity.Mascota;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.MascotaRepo;
import com.example.demo.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaService {
    @Autowired
    private MascotaRepo mascotaRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    public Mascota registrarMascota(Long usuarioId, Mascota mascota) {
        Usuario duenio = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        mascota.setDuenio(duenio);
        return mascotaRepo.save(mascota);
    }

    public List<Mascota> buscarMascotasPorNombre(String nombre) {
        return mascotaRepo.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Mascota> buscarDelUsuario(Long usuarioId, String nombre) {
        if (!usuarioRepo.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }
        return mascotaRepo.findByDuenioIdAndNombreContainingIgnoreCase(usuarioId, nombre);
    }
}


