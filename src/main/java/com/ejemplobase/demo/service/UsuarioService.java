package com.ejemplobase.demo.service;

import com.ejemplobase.demo.model.dao.CargoDao;
import com.ejemplobase.demo.model.dao.UsuarioDao;
import com.ejemplobase.demo.model.dto.LoginDto;
import com.ejemplobase.demo.model.dto.RegisterDto;
import com.ejemplobase.demo.model.dto.UsuarioDto;
import com.ejemplobase.demo.model.dto.UsuarioEditarDto;
import com.ejemplobase.demo.model.entity.Cargo;
import com.ejemplobase.demo.model.entity.Usuario;
import com.ejemplobase.demo.service.interfaces.IUsuarioService;
import com.ejemplobase.demo.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private CargoDao cargoDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    @Transactional
    public Usuario registrar_usuario(RegisterDto registerDto) {

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, registerDto.getContraseña());
        Cargo cargo=cargoDao.findById(registerDto.getCargoid()).orElse(null);
        if(cargo==null){
            return null;
        }
        Usuario usuario = Usuario.builder()
                .nombreCompleto(registerDto.getNombreCompleto())
                .correo(registerDto.getCorreo())
                .contraseña(hash)
                .cargo(cargo)
                .build();
        return usuarioDao.save(usuario);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Usuario> obtener_usuarios() {

        return (List<Usuario>) usuarioDao.findAll();
    }

    @Override
    @Transactional(readOnly=true)
    public Usuario obtener_usuario(Integer id) {

        return usuarioDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Usuario editar_usuario(UsuarioEditarDto usuarioEditarDto, Integer id) {

        Usuario usuario_existente=usario_exit_id(id);
        if(usuario_existente==null){
            return null;
        }
        Cargo cargo=cargoDao.findById(usuarioEditarDto.getCargoid()).orElse(null);
        if(cargo==null){
            return null;
        }
        usuario_existente.setNombreCompleto(usuarioEditarDto.getNombreCompleto());
        usuario_existente.setCargo(cargo);
        return usuarioDao.save(usuario_existente);
    }

    @Override
    @Transactional
    public void eliminar_usuario(Integer id) {

        usuarioDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly=true)
    public Usuario obtener_usuario_mediante_credenciales(LoginDto loginDto) {

        Usuario usuario=usuario_existent(loginDto.getCorreo());
        if(usuario==null){
            return null;
        }
        else{
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            if (argon2.verify(usuario.getContraseña(), loginDto.getContraseña())) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly=true)
    public Usuario usuario_existent(String correo) {

        Usuario usuario_exisstente=usuarioDao.findByCorreo(correo).orElse(null);
        return usuario_exisstente;
    }

    @Override
    public Usuario usario_exit_id(Integer id) {

        Usuario usuario=usuarioDao.findById(id).orElse(null);
        return usuario;
    }

    @Override
    public Page<Usuario> obtenerusuariopaginado(Integer page, Integer size, String searchTerm) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").ascending());
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return usuarioDao.findByNombreContainingIgnoreCase(searchTerm, pageable);
        } else {
            return usuarioDao.findAll(pageable);
        }
    }
}
