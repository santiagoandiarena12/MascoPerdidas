package com.example.demo.service;

import com.example.demo.entity.Publicacion;
import com.example.demo.entity.TipoPublicacion;
import com.example.demo.entity.Usuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarAlertaCercana(Usuario u, Publicacion p, double dist) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Usamos el Enum para decidir el estilo
            boolean esPerdida = p.getTipo() == TipoPublicacion.PERDIDA;
            String colorPrincipal = esPerdida ? "#d9534f" : "#5bc0de";

            helper.setTo(u.getEmail());
            helper.setSubject((esPerdida ? "🚨 PERDIDA: " : "🏠 ENCONTRADA: ") + p.getMascota().getNombre());

            String contenidoHtml =
                    "<div style='font-family: Arial; border: 2px solid " + colorPrincipal + "; padding: 20px;'>" +
                            "<h2 style='color: " + colorPrincipal + ";'>¡Hola " + u.getNombre() + "!</h2>" +
                            "<p>Esta mascota se encuentra a <strong>" + String.format("%.2f", dist) + " km</strong> de tu casa.</p>" +
                            "<img src='" + p.getMascota().getFotoUrl() + "' style='width: 100%; max-width: 300px;'>" +
                            "<p><strong>Descripción:</strong> " + p.getDescripcion() + "</p>" +
                            "</div>";

            helper.setText(contenidoHtml, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el email HTML", e);
        }
    }
}
