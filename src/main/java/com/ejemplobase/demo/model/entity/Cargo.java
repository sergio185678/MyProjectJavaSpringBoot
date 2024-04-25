package com.ejemplobase.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.nio.MappedByteBuffer;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="cargo")
public class Cargo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cargo_id")
    private Integer cargoId;

    @Column(name="nombre")
    private String nombre;

    @OneToMany(mappedBy = "cargo")
    @JsonIgnore
    private List<Usuario> usuarios;

}
