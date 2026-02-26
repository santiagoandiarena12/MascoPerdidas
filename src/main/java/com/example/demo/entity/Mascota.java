package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(max = 50, message = "El nombre de la mascota no puede exceder 50 caracteres")
    private String nombre;

    @NotBlank(message = "La especie de la mascota es obligatoria")
    @Size(max = 50, message = "La especie de la mascota no puede exceder 50 caracteres")
    private String especie;

    @Size(max = 50, message = "La raza no puede exceder 50 caracteres")
    private String raza;

    @Size(max = 255, message = "La URL de la foto no puede exceder 255 caracteres")
    private String fotoUrl;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIncludeProperties({"id"})
    private Usuario duenio;

    @OneToMany(mappedBy = "mascota")
    private List<Publicacion> historialPublicaciones;
}
