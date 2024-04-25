package com.ejemplobase.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @Column(name="nombre_completo")
    private String nombreCompleto;

    @Column(name="correo")
    private String correo;

    @Column(name="contraseña")
    private String contraseña;

    @ManyToOne
    @JoinColumn(name="id_cargo")
    private Cargo cargo;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Documento> documentos;

}
