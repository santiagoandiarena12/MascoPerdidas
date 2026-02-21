package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReporteMascotasCercanasDTO {

    private String titulo;
    private String nombreMascota;
    private String especie;
    private String descripcion;
    private Double latitudReporte;
    private Double longitudReporte;
    private Double distanciaKm;


}
