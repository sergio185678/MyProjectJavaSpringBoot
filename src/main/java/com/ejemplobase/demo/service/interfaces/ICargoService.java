package com.ejemplobase.demo.service.interfaces;

import com.ejemplobase.demo.model.entity.Cargo;
import com.ejemplobase.demo.model.entity.Usuario;

import java.util.List;

public interface ICargoService {
    Cargo registrarCargo(Cargo cargo);
    List<Cargo> obtenerCargos();
    Cargo obtenerCargo(Integer cargoid);
    List<Usuario> obtenerUsuariosPorCargo(Integer cargoid);
}
