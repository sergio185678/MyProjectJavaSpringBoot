package com.ejemplobase.demo.service;

import com.ejemplobase.demo.model.dao.DocumentoDao;
import com.ejemplobase.demo.model.dao.UsuarioDao;
import com.ejemplobase.demo.model.entity.Documento;
import com.ejemplobase.demo.model.entity.Usuario;
import com.ejemplobase.demo.service.interfaces.IDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentoService implements IDocumentoService {
    @Autowired
    private DocumentoDao documentoDao;

    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    public Documento registrarDocumento(MultipartFile file, String tipo, Integer usuarioid) {

        // Generar un nombre único para el archivo
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        String carpetaRelativa = "src/main/resources/uploads";
        File carpeta = new File(carpetaRelativa);
        String UPLOAD_DIR = carpeta.getAbsolutePath();

        String filePath = UPLOAD_DIR + File.separator + fileName;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
        }
        catch (IOException e){
            return null;
        }

        Usuario usuario=usuarioDao.findById(usuarioid).orElse(null);
        if(usuario==null){
            return null;
        }

        Documento doc=Documento.builder()
                .tipo(tipo)
                .ruta(fileName)
                .usuario(usuario)
                .build();

        return  documentoDao.save(doc);
    }

    @Override
    public List<Documento> obtenerListaDocumentosUsuario(Integer userid) {
        return documentoDao.findbyuser(userid);
    }

    @Override
    public void eliminar_documento(Integer id) {
        Documento doc=this.documentoDao.findById(id).orElse(null);

        String carpetaRelativa = "src/main/resources/uploads";
        File carpeta = new File(carpetaRelativa);
        String UPLOAD_DIR = carpeta.getAbsolutePath();

        String filePath = UPLOAD_DIR + File.separator + doc.getRuta();
        File fileToDelete = new File(filePath);

        if (fileToDelete.exists()) {

            if (fileToDelete.delete()) {
                this.documentoDao.deleteById(id);
            }
        }

    }
}
