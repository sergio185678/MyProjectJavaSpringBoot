package com.ejemplobase.demo.model.dto;

import com.ejemplobase.demo.model.entity.Cargo;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    private String nombreCompleto;
    private String correo;
    private String contraseña;
    private Integer cargoid;

}
