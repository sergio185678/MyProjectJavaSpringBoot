package com.ejemplobase.demo.controller;

import com.ejemplobase.demo.middleware.IAuthenticationMiddleware;
import com.ejemplobase.demo.model.dto.LoginDto;
import com.ejemplobase.demo.model.dto.RegisterDto;
import com.ejemplobase.demo.model.dto.UsuarioDto;
import com.ejemplobase.demo.model.entity.Usuario;
import com.ejemplobase.demo.model.payload.MensajeResponse;
import com.ejemplobase.demo.service.interfaces.IUsuarioService;
import com.ejemplobase.demo.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private IAuthenticationMiddleware authenticationMiddleware;

    @PostMapping("register")
    public ResponseEntity<?> registrar_usuario(@RequestBody RegisterDto registerDto){

        if(usuarioService.usuario_existent(registerDto.getCorreo())!=null){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El usuario ya existe. Por favor, elige otro correo electrónico.")
                            .object(null)
                            .build()
                    , HttpStatus.CONFLICT);
        }
        Usuario usuario =usuarioService.registrar_usuario(registerDto);
        if(usuario==null){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Error al registrar.")
                            .object(null)
                            .build()
                    , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Se registro correctamente.")
                        .object(convertirAUsuarioDTO(usuario))
                        .build()
                , HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){

        Usuario usuariologueado=usuarioService.obtener_usuario_mediante_credenciales(loginDto);
        if (usuariologueado != null) {
            //aca en el JWTUtil configurar con las necesidad si quieres agregar mas cosas como rol
            String tokenJwt = jwtUtil.create(String.valueOf(usuariologueado.getUserId()), usuariologueado.getNombreCompleto(),usuariologueado.getCargo().getNombre());
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Se logueo correctamente")
                            .object(tokenJwt)
                            .build()
                    , HttpStatus.OK);
        }
        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Correo o contraseña incorrecta")
                        .object(null)
                        .build()
                , HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/validar_token")
    public Boolean validar_token(@RequestHeader("Authorization") String token){

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticate(token);
        if(authenticated==null){
            return true;
        }
        return false;
    }

    @GetMapping("/info_user_jwt")
    public ResponseEntity<?> get_info_jwt(@RequestHeader("Authorization") String token){

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticate(token);
        if(authenticated!=null){
            return authenticated;
        }
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        return new  ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .object(jwtUtil.getClaims(token))
                        .build()
                ,HttpStatus.OK);
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
