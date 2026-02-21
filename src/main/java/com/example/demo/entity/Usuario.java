package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String telefono;

    // Coordenadas del domicilio para el sistema de alertas por radio de 2km
    private Double latitudCasa;
    private Double longitudCasa;

    // Un usuario puede tener muchas mascotas asociadas
    @OneToMany(mappedBy = "duenio", cascade = CascadeType.ALL)
    private List<Mascota> mascotas;
}
