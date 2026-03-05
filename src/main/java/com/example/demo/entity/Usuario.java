package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import com.example.demo.validation.CreateValidation;

import java.util.List;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.ROLE_USER;

    @NotBlank(message = "El nombre del usuario es obligatorio", groups = CreateValidation.class)
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El email del usuario es obligatorio", groups = CreateValidation.class)
    @Email(message = "El formato del email es invalido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "La contrasena es obligatoria", groups = CreateValidation.class)
    @Size(min = 6, max = 100, message = "La contrasena debe tener entre 6 y 100 caracteres")
    private String password;

    private String telefono;

    // Coordenadas del domicilio para el sistema de alertas por radio de 2km
    @DecimalMin(value = "-90.0", message = "Latitud fuera de rango")
    @DecimalMax(value = "90.0", message = "Latitud fuera de rango")
    private Double latitudCasa;

    @DecimalMin(value = "-180.0", message = "Longitud fuera de rango")
    @DecimalMax(value = "180.0", message = "Longitud fuera de rango")
    private Double longitudCasa;

    // Un usuario puede tener muchas mascotas asociadas
    @OneToMany(mappedBy = "duenio", cascade = CascadeType.ALL)
    private List<Mascota> mascotas;
}
