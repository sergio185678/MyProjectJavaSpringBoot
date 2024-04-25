package com.ejemplobase.demo.model.dao;

import com.ejemplobase.demo.model.entity.Cargo;
import org.springframework.data.repository.CrudRepository;

public interface CargoDao extends CrudRepository<Cargo,Integer> {
}
