package com.ejemplobase.demo.controller;

import com.ejemplobase.demo.middleware.IAuthenticationMiddleware;
import com.ejemplobase.demo.model.dto.DocumentoDto;
import com.ejemplobase.demo.model.entity.Documento;
import com.ejemplobase.demo.model.payload.MensajeResponse;
import com.ejemplobase.demo.service.interfaces.IDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/document")
public class DocumentoController {

    @Autowired
    private IDocumentoService documentoService;

    @Autowired
    private IAuthenticationMiddleware authenticationMiddleware;

    @PostMapping("/save_files")
    public ResponseEntity<?> guardar_archivos(@RequestParam("file") MultipartFile file,@RequestParam("tipo") String tipo,
                                              @RequestParam("usuarioid") int usuarioid,@RequestHeader("Authorization") String token) {

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticatePorCargo(token,"Administrador");
        if(authenticated!=null){
            return authenticated;
        }

        if (file.isEmpty()) {
            return new  ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Error al enviar archivo.")
                            .object(null)
                            .build()
                    , HttpStatus.BAD_REQUEST);
        }
        Documento documento = documentoService.registrarDocumento(file, tipo,usuarioid);
        if(documento==null){
            return new  ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Error al registrar.")
                            .object(null)
                            .build()
                    ,HttpStatus.BAD_REQUEST);
        }
        return new  ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Se registro el archivo correctamente.")
                        .object(null)
                        .build()
                ,HttpStatus.OK);
    }

    @GetMapping("/get_all_files_by_user/{user_id}")
    public ResponseEntity<?> obtener_archivos(@PathVariable Integer user_id,@RequestHeader("Authorization") String token){

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticate(token);
        if(authenticated!=null){
            return authenticated;
        }

        List<Documento> listdocs=documentoService.obtenerListaDocumentosUsuario(user_id);
        return new  ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Lista de documentos.")
                        .object(listdocs.stream()
                                .map(this::convertirADocumentoDTO)
                                .collect(Collectors.toList()))
                        .build()
                ,HttpStatus.OK);
    }

    @GetMapping("/get_url/{id}")
    public ResponseEntity<?> obtener_ruta(@PathVariable Integer id) {
        Documento documento=documentoService.obtenerDocumento(id);
        return new  ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Ruta del archivo.")
                        .object(documento.getRuta())
                        .build()
                ,HttpStatus.OK);
    }

    @DeleteMapping("/file/{id}")
    public void eliminar_Archivo(@PathVariable Integer id,@RequestHeader("Authorization") String token) {

        ResponseEntity<?> authenticated=authenticationMiddleware.authenticatePorCargo(token,"Administrador");
        if(authenticated==null){
            this.documentoService.eliminar_documento(id);
        }
    }

    public String obtener_rutapadre(){
        String carpetaRelativa = "src/main/resources/uploads";
        File carpeta = new File(carpetaRelativa);
        String UPLOAD_DIR = carpeta.getAbsolutePath();
        return UPLOAD_DIR;
    }

    private DocumentoDto convertirADocumentoDTO(Documento documento) {

        DocumentoDto documentoDto = new DocumentoDto();
        documentoDto.setDocumentoId(documento.getDocumentoId());
        documentoDto.setTipo(documento.getTipo());
        documentoDto.setRuta(documento.getRuta());
        documentoDto.setUsuarioid(documento.getUsuario().getUserId());
        return documentoDto;
    }
}
