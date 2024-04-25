package com.ejemplobase.demo.model.dto;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEditarDto {

    private String nombreCompleto;
    private Integer cargoid;

}
