package com.ejemplobase.demo.model.dto;

import com.ejemplobase.demo.model.entity.Cargo;
import lombok.*;

import java.io.Serializable;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto implements Serializable {

    private Integer userId;
    private String nombreCompleto;
    private String correo;
    private Cargo cargo;

}
