package com.ejemplobase.demo.model.dao;

import com.ejemplobase.demo.model.entity.Documento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentoDao extends CrudRepository<Documento,Integer> {
    @Query("SELECT d FROM Documento d WHERE d.usuario.userId = :userId")
    List<Documento> findbyuser(@Param("userId") Integer userId);
}
