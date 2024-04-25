package com.ejemplobase.demo.controller;

import com.ejemplobase.demo.middleware.IAuthenticationMiddleware;
import com.ejemplobase.demo.model.dto.UsuarioDto;
import com.ejemplobase.demo.model.dto.UsuarioEditarDto;
import com.ejemplobase.demo.model.entity.Usuario;
import com.ejemplobase.demo.model.payload.MensajeResponse;
import com.ejemplobase.demo.service.interfaces.IUsuarioService;
import com.ejemplobase.demo.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/user")
public class UsuarioController {
    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IAuthenticationMiddleware authenticationMiddleware;
    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("usuarios")
    public ResponseEntity<?> obtener_usuarios(@RequestHeader("Authorization") String token){

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticate(token);
        if(authenticated!=null){
            return authenticated;
        }

        List<Usuario> usuarios=usuarioService.obtener_usuarios();
        if(usuarios==null){
            return new  ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No hay usuarios.")
                            .object(null)
                            .build()
            ,HttpStatus.OK);
        }
        return new  ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("lista de usuarios")
                        .object(usuarios.stream()
                                .map(this::convertirAUsuarioDTO)
                                .collect(Collectors.toList()))
                        .build()
                ,HttpStatus.OK);
    }

    @GetMapping("usuario/{id}")
    public ResponseEntity<?> obtener_usuario(@PathVariable Integer id,@RequestHeader("Authorization") String token){

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticate(token);
        if(authenticated!=null){
            return authenticated;
        }

        Usuario usuario=usuarioService.obtener_usuario(id);
        if(usuario==null){
            return new  ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No se encontro el usuario.")
                            .object(null)
                            .build()
                    ,HttpStatus.NOT_FOUND);
        }
        return new  ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .object(convertirAUsuarioDTO(usuario))
                        .build()
                ,HttpStatus.OK);
    }

    @PutMapping("usuario/{id}")
    public ResponseEntity<?> editar_usuario(@RequestBody UsuarioEditarDto usuarioEditarDto, @PathVariable Integer id, @RequestHeader("Authorization") String token){

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticatePorCargo(token,"Administrador");
        if(authenticated!=null){
            return authenticated;
        }

        Usuario usuario=usuarioService.editar_usuario(usuarioEditarDto,id);
        if(usuario==null){
            return new  ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No se encontro el usuario.")
                            .object(null)
                            .build()
                    ,HttpStatus.NOT_FOUND);
        }
        return new  ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Se actualizo correctamente.")
                        .object(convertirAUsuarioDTO(usuario))
                        .build()
                ,HttpStatus.OK);
    }

    @DeleteMapping("usuario/{id}")
    public void eliminar_usuario(@PathVariable Integer id,@RequestHeader("Authorization") String token){

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticatePorCargo(token,"Administrador");
        if(authenticated==null){
            usuarioService.eliminar_usuario(id);
        }
    }

    @GetMapping("/usuariospaginacion")
    public ResponseEntity<?> obtenerUsuariosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm
            ,@RequestHeader("Authorization") String token){

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticate(token);
        if(authenticated!=null){
            return authenticated;
        }

        Page<Usuario> usuariosPage = usuarioService.obtenerusuariopaginado(page,size,searchTerm);
        Page<UsuarioDto> usuariosDtoPage = usuariosPage.map(this::convertirAUsuarioDTO);
        return new  ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .object(usuariosDtoPage)
                        .build()
                ,HttpStatus.OK);
    }

    @GetMapping("/user_by_jwt")
    public ResponseEntity<?> obtenerusuarioporjwt(@RequestHeader("Authorization") String token){

        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        return obtener_usuario(Integer.parseInt(jwtUtil.getKey(token)),token);
    }

    private UsuarioDto convertirAUsuarioDTO(Usuario usuario) {

        UsuarioDto usuarioDTO = new UsuarioDto();
        usuarioDTO.setUserId(usuario.getUserId());
        usuarioDTO.setNombreCompleto(usuario.getNombreCompleto());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setCargo(usuario.getCargo());
        return usuarioDTO;
    }
}
