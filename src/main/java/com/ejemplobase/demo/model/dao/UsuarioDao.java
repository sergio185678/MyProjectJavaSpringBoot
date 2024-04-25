package com.ejemplobase.demo.model.dao;

import com.ejemplobase.demo.model.dto.UsuarioDto;
import com.ejemplobase.demo.model.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioDao extends CrudRepository<Usuario,Integer>, PagingAndSortingRepository<Usuario,Integer> {
    Optional<Usuario> findByCorreo(String correo);

    @Query("SELECT u FROM Usuario u WHERE u.cargo.cargoId = :cargoId")
    List<Usuario> findByCargoId(@Param("cargoId") Integer cargoId);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombreCompleto) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Usuario> findByNombreContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
}
