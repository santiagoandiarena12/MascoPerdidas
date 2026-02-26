package com.example.demo.service;

import com.example.demo.dto.ReporteMascotasCercanasDTO;
import com.example.demo.entity.Mascota;
import com.example.demo.entity.Publicacion;
import com.example.demo.entity.TipoPublicacion;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.MascotaRepo;
import com.example.demo.repository.PublicacionRepo;
import com.example.demo.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacionService {

    @Autowired
    private PublicacionRepo publicacionRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private MascotaRepo mascotaRepo;

    @Autowired
    private EmailService emailService;

    // CREATE
    public Publicacion crearPublicacion(Long uId, Long mId, Publicacion p) {
        Usuario autor = usuarioRepo.findById(uId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Mascota mascota = mascotaRepo.findById(mId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (mascota.getDuenio() == null || !mascota.getDuenio().getId().equals(uId)) {
            throw new RuntimeException("La mascota no pertenece al usuario indicado");
        }

        // Validar que no exista ya una publicación PERDIDA para esta mascota
        if (p.getTipo() == TipoPublicacion.PERDIDA &&
            publicacionRepo.existsByMascotaIdAndTipo(mId, TipoPublicacion.PERDIDA)) {
            throw new RuntimeException("Ya existe una publicacion PERDIDA para esta mascota");
        }

        p.setAutor(autor);
        p.setMascota(mascota);
        Publicacion nueva = publicacionRepo.save(p);

        if (p.getTipo() == TipoPublicacion.PERDIDA) {
            System.out.println("Iniciando alerta vecinal para: " + mascota.getNombre());
            List<Usuario> todos = usuarioRepo.findAll();

            for (Usuario u : todos) {
                double dist = calcularDistancia(p.getLatitudReporte(), p.getLongitudReporte(),
                        u.getLatitudCasa(), u.getLongitudCasa());

                if (!u.getId().equals(uId) && dist <= 2.0) {
                    notificarVecino(u, p, dist);
                    esperarParaMailtrap();
                }
            }
        }
        else if (p.getTipo() == TipoPublicacion.ENCONTRADA) {
            Usuario duenio = mascota.getDuenio();

            if (!duenio.getId().equals(uId)) {
                System.out.println("Mascota encontrada! Avisando al dueño: " + duenio.getNombre());
                notificarVecino(duenio, p, 0.0);
            }
        }

        return nueva;
    }

    // READ - Obtener todas
    public List<Publicacion> listarTodas() {
        return publicacionRepo.findAll();
    }

    // READ - Obtener por ID
    public Optional<Publicacion> obtenerPorId(Long id) {
        return publicacionRepo.findById(id);
    }

    // READ - Obtener por tipo
    public List<Publicacion> obtenerPorTipo(TipoPublicacion tipo) {
        return publicacionRepo.findByTipo(tipo);
    }

    // READ - Obtener por autor
    public List<Publicacion> obtenerPorAutor(Long usuarioId) {
        return publicacionRepo.findByAutorId(usuarioId);
    }

    // UPDATE
    public Publicacion actualizarPublicacion(Long id, Publicacion publicacionActualizada) {
        return publicacionRepo.findById(id)
                .map(p -> {
                    p.setTitulo(publicacionActualizada.getTitulo());
                    p.setDescripcion(publicacionActualizada.getDescripcion());
                    p.setTipo(publicacionActualizada.getTipo());
                    p.setLatitudReporte(publicacionActualizada.getLatitudReporte());
                    p.setLongitudReporte(publicacionActualizada.getLongitudReporte());
                    return publicacionRepo.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
    }

    // DELETE
    public void eliminarPublicacion(Long id) {
        if (!publicacionRepo.existsById(id)) {
            throw new RuntimeException("Publicación no encontrada");
        }
        publicacionRepo.deleteById(id);
    }

    // Métodos auxiliares existentes
    private void esperarParaMailtrap() {
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void notificarVecino(Usuario u, Publicacion p, double dist) {
        try {
            emailService.enviarAlertaCercana(u, p, dist);
            System.out.println("!! EMAIL ENVIADO A: " + u.getNombre());
        } catch (Exception e) {
            System.err.println("Fallo el envío a " + u.getNombre() + " por límite de Mailtrap");
        }
    }

    public List<ReporteMascotasCercanasDTO> buscarCercanas(Double latUser, Double longUser, Double radioKm) {
        List<Publicacion> todas = publicacionRepo.findAll();
        List<ReporteMascotasCercanasDTO> cercanas = new ArrayList<>();

        for(Publicacion p : todas){
            double distancia = calcularDistancia(latUser, longUser, p.getLatitudReporte(), p.getLongitudReporte());

            if(distancia <= radioKm){
                cercanas.add( new ReporteMascotasCercanasDTO(
                        p.getTitulo(),
                        p.getMascota().getNombre(),
                        p.getMascota().getEspecie(),
                        p.getMascota().getFotoUrl(),
                        p.getDescripcion(),
                        p.getLatitudReporte(),
                        p.getLongitudReporte(),
                        distancia
                ));
            }
        }
        return cercanas;
    }

    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2){
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
