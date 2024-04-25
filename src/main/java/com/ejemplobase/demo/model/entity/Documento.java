package com.ejemplobase.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="documento")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_documento")
    private Integer documentoId;

    @Column(name="tipo_documento")
    private String tipo;

    @Column(name="ruta")
    private String ruta;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

}
