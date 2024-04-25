package com.ejemplobase.demo.middleware;

import com.ejemplobase.demo.model.payload.MensajeResponse;
import com.ejemplobase.demo.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationMiddleware implements IAuthenticationMiddleware{
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public ResponseEntity<?> authenticate(String token) {

        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        //aca suele dar error 500 ya que no puede validar algo, por eso try y catch
        try{
            if (!validarToken(token)) {
                return new  ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("Necesita autentificación.")
                                .object(null)
                                .build()
                        ,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception err){
            return new  ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Error en la autentificación.")
                            .object(null)
                            .build()
                    ,HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> authenticatePorCargo(String token, String cargo) {

        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        if(authenticate(token)==null){
            String cargousuario=jwtUtil.getValueclaim(token,"cargo");
            if(cargousuario.equals(cargo)){
                return null;
            }
            else{
                return new  ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("No estas autorizado.")
                                .object(null)
                                .build()
                        ,HttpStatus.UNAUTHORIZED);
            }
        }else{
            return authenticate(token);
        }
    }

    @Override
    public Boolean validarToken(String token) {

        String usuarioId = jwtUtil.getKey(token);
        return usuarioId != null;
    }
}
