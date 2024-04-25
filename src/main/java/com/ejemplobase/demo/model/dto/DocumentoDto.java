package com.ejemplobase.demo.model.dto;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoDto {
    private Integer documentoId;
    private String tipo;
    private String ruta;
    private Integer usuarioid;
}
