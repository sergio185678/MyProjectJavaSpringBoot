package com.ejemplobase.demo.controller;

import com.ejemplobase.demo.middleware.IAuthenticationMiddleware;
import com.ejemplobase.demo.model.dto.CargoDto;
import com.ejemplobase.demo.model.dto.UsuarioDto;
import com.ejemplobase.demo.model.entity.Cargo;
import com.ejemplobase.demo.model.entity.Usuario;
import com.ejemplobase.demo.model.payload.MensajeResponse;
import com.ejemplobase.demo.service.interfaces.ICargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/role")
public class CargoController {
    @Autowired
    private ICargoService cargoService;
    @Autowired
    private IAuthenticationMiddleware authenticationMiddleware;

    @PostMapping("register")
    public ResponseEntity<?> registrar_cargo(@RequestBody Cargo cargo,@RequestHeader("Authorization") String token) {

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticatePorCargo(token,"Administrador");
        if(authenticated!=null){
            return authenticated;
        }

        Cargo cargoo= cargoService.registrarCargo(cargo);
        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Se registro correctamente.")
                        .object(convertirACargoDTO(cargoo))
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("cargos")
    public ResponseEntity<?> obtenercargos(@RequestHeader("Authorization") String token){

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticate(token);
        if(authenticated!=null){
            return authenticated;
        }

        List<Cargo> cargos=cargoService.obtenerCargos();
        if(cargos==null){
            return new  ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No hay cargos.")
                            .object(null)
                            .build()
                    ,HttpStatus.OK);
        }
        return new  ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("lista de cargos")
                        .object(cargos.stream()
                                .map(this::convertirACargoDTO)
                                .collect(Collectors.toList()))
                        .build()
                ,HttpStatus.OK);
    }

    @GetMapping("usuarios_by_cargo/{cargo_id}")
    public ResponseEntity<?> obtenerUsuariosPorCargo(@PathVariable Integer cargo_id,@RequestHeader("Authorization") String token){

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticate(token);
        if(authenticated!=null){
            return authenticated;
        }

        List<Usuario> usuarios=cargoService.obtenerUsuariosPorCargo(cargo_id);
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

    @GetMapping("cargo/{id}")
    public ResponseEntity<?> obtener_cargo(@PathVariable Integer id,@RequestHeader("Authorization") String token){
        ResponseEntity<?> authenticated=authenticationMiddleware.authenticate(token);
        if(authenticated!=null){
            return authenticated;
        }

        Cargo cargo=cargoService.obtenerCargo(id);
        if(cargo==null){
            return new  ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No se encontro el cargo.")
                            .object(null)
                            .build()
                    ,HttpStatus.NOT_FOUND);
        }
        return new  ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .object(convertirACargoDTO(cargo))
                        .build()
                ,HttpStatus.OK);
    }


    private CargoDto convertirACargoDTO(Cargo cargo) {

        CargoDto cargoDto = new CargoDto();
        cargoDto.setCargoId(cargo.getCargoId());
        cargoDto.setNombre(cargo.getNombre());
        return cargoDto;
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
