package com.example.demo.service;

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

    public void enviarAlertaCercana(String destinatario, String nombreVecino, String mascota, String descripcion, String fotoUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject("🚨 ¡ALERTA! Mascota perdida cerca de tu zona: " + mascota);

            // Cuerpo del mail en HTML
            String contenidoHtml =
                    "<div style='font-family: Arial, sans-serif; border: 1px solid #ddd; padding: 20px; max-width: 600px;'>" +
                            "<h2 style='color: #d9534f;'>¡Hola " + nombreVecino + "!</h2>" +
                            "<p style='font-size: 16px;'>Un miembro de la comunidad de <strong>CuidadorCercano</strong> necesita tu ayuda.</p>" +
                            "<div style='text-align: center; margin: 20px 0;'>" +
                            "<img src='" + fotoUrl + "' alt='Foto de la mascota' style='width: 200px; border-radius: 10px;'>" +
                            "</div>" +
                            "<p style='font-size: 18px;'>Se perdió <strong>" + mascota + "</strong> cerca de tu ubicación.</p>" +
                            "<blockquote style='background: #f9f9f9; padding: 10px; border-left: 5px solid #ccc;'>" +
                            descripcion +
                            "</blockquote>" +
                            "<div style='text-align: center; margin-top: 30px;'>" +
                            "<a href='#' style='background-color: #5cb85c; color: white; padding: 15px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;'>Avisar al dueño</a>" +
                            "</div>" +
                            "<p style='font-size: 12px; color: #777; margin-top: 40px;'>Este es un aviso automático basado en tu ubicación registrada.</p>" +
                            "</div>";

            helper.setText(contenidoHtml, true); // El 'true' activa el modo HTML
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el email HTML", e);
        }
    }
}
