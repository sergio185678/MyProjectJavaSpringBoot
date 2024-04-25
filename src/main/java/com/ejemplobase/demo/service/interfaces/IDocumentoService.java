package com.ejemplobase.demo.service.interfaces;

import com.ejemplobase.demo.model.entity.Documento;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDocumentoService {
    Documento registrarDocumento(MultipartFile file, String tipo, Integer usuarioid);
    List<Documento> obtenerListaDocumentosUsuario(Integer userid);
    void eliminar_documento(Integer id);
}
