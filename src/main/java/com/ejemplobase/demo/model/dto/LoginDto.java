package com.ejemplobase.demo.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class LoginDto {

    private String correo;
    private String contraseña;

}
