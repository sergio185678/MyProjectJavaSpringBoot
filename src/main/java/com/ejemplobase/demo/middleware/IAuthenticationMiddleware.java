package com.ejemplobase.demo.middleware;

import org.springframework.http.ResponseEntity;

public interface IAuthenticationMiddleware {
    ResponseEntity<?> authenticate(String token);

    ResponseEntity<?> authenticatePorCargo(String token, String cargo);

    Boolean validarToken(String token);
}
