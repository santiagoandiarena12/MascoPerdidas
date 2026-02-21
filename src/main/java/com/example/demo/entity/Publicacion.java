package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;

    private TipoPublicacion tipo;

    // Ubicación exacta de ESTE reporte
    private Double latitudReporte;
    private Double longitudReporte;

    private LocalDateTime fechaCarga = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario autor;

    @ManyToOne
    @JoinColumn(name = "mascota_id")
    @JsonIgnore
    private Mascota mascota;
}
