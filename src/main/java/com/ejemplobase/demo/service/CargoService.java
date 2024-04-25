package com.ejemplobase.demo.service;

import com.ejemplobase.demo.model.dao.CargoDao;
import com.ejemplobase.demo.model.dao.UsuarioDao;
import com.ejemplobase.demo.model.entity.Cargo;
import com.ejemplobase.demo.model.entity.Usuario;
import com.ejemplobase.demo.service.interfaces.ICargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class CargoService implements ICargoService {
    @Autowired
    private CargoDao cargoDao;
    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    public Cargo registrarCargo(Cargo cargo) {

        Cargo cargoo= Cargo.builder()
                .nombre(cargo.getNombre())
                .build();
        return cargoDao.save(cargoo);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Cargo> obtenerCargos() {

        return (List<Cargo>) cargoDao.findAll();
    }

    @Override
    public Cargo obtenerCargo(Integer cargoid) {
        return cargoDao.findById(cargoid).orElse(null);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Usuario> obtenerUsuariosPorCargo(Integer cargoid) {

        return usuarioDao.findByCargoId(cargoid);
    }
}
