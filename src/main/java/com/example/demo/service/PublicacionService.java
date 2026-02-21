package com.example.demo.service;

import com.example.demo.dto.ReporteMascotasCercanasDTO;
import com.example.demo.entity.Mascota;
import com.example.demo.entity.Publicacion;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.MascotaRepo;
import com.example.demo.repository.PublicacionRepo;
import com.example.demo.repository.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public Publicacion crearPublicacion(Long uId, Long mId, Publicacion p) {
        Usuario autor = usuarioRepo.findById(uId).orElseThrow();
        Mascota mascota = mascotaRepo.findById(mId).orElseThrow();

        p.setAutor(autor);
        p.setMascota(mascota);
        Publicacion nueva = publicacionRepo.save(p);

        List<Usuario> todos = usuarioRepo.findAll();

        for (Usuario u : todos) {
            double dist = calcularDistancia(p.getLatitudReporte(), p.getLongitudReporte(),
                    u.getLatitudCasa(), u.getLongitudCasa());

            if (!u.getId().equals(uId) && dist <= 2.0) {
                System.out.println("!!ALERTA VECINAL ENCONTRADA: " + u.getNombre());
                notificarVecino(u, p);
            }
        }
        return nueva;
    }

    private void notificarVecino(Usuario u, Publicacion p) {
        emailService.enviarAlertaCercana(
                u.getEmail(),
                u.getNombre(),
                p.getMascota().getNombre(),
                p.getDescripcion(),
                p.getMascota().getFotoUrl()
        );
        System.out.println("!! EMAIL HTML ENVIADO A: " + u.getNombre());
    }

    public List<Publicacion> listarTodas() {
        return publicacionRepo.findAll();
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
                            p.getDescripcion(),
                            p.getLatitudReporte(),
                            p.getLongitudReporte(),
                            distancia
                    ));
            }
        }
        return cercanas;
    }

    //calcular distancia mediante formula
    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2){
        double earthRadius = 6371; // Kilómetros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

}
