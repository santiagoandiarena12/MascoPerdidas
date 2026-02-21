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

        // Lógica diferenciada por Tipo
        if (p.getTipo() == TipoPublicacion.PERDIDA) {
            System.out.println("Iniciando alerta vecinal para: " + mascota.getNombre());
            List<Usuario> todos = usuarioRepo.findAll();

            for (Usuario u : todos) {
                double dist = calcularDistancia(p.getLatitudReporte(), p.getLongitudReporte(),
                        u.getLatitudCasa(), u.getLongitudCasa());

                // Avisar a vecinos en 2km (excluyendo al autor)
                if (!u.getId().equals(uId) && dist <= 2.0) {
                    notificarVecino(u, p, dist);
                    esperarParaMailtrap(); // Pausa para evitar el error
                }
            }
        }
        else if (p.getTipo() == TipoPublicacion.ENCONTRADA) {
            //Avisar al dueño original de la mascota
            Usuario duenio = mascota.getDuenio();

            // Solo avisamos si el que la encuentra no es el propio dueño
            if (!duenio.getId().equals(uId)) {
                System.out.println("Mascota encontrada! Avisando al dueño: " + duenio.getNombre());
                notificarVecino(duenio, p, 0.0);
            }
        }

        return nueva;
    }

    // Mtodo auxiliar para no saturar Mailtrap
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
