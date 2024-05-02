package com.ejemplobase.demo.service.interfaces;

import com.ejemplobase.demo.model.dto.LoginDto;
import com.ejemplobase.demo.model.dto.RegisterDto;
import com.ejemplobase.demo.model.dto.UsuarioDto;
import com.ejemplobase.demo.model.dto.UsuarioEditarDto;
import com.ejemplobase.demo.model.entity.Usuario;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUsuarioService {
    Usuario registrar_usuario(RegisterDto registerDto);
    List<Usuario> obtener_usuarios();
    Usuario obtener_usuario(Integer id);
    Usuario editar_usuario(UsuarioEditarDto usuarioEditarDto, Integer id);
    void eliminar_usuario(Integer id);
    Usuario obtener_usuario_mediante_credenciales(LoginDto loginDto);
    Usuario usuario_existent(String correo);
    Page<Usuario> obtenerusuariopaginado(Integer page, Integer size, String searchTerm);
}
