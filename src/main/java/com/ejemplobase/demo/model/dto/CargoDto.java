package com.ejemplobase.demo.model.dto;

import com.ejemplobase.demo.model.entity.Cargo;
import lombok.*;

import java.io.Serializable;
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoDto implements Serializable {

    private Integer cargoId;
    private String nombre;

}
