package com.ejemplobase.demo.service;

import com.ejemplobase.demo.model.dao.DocumentoDao;
import com.ejemplobase.demo.model.dao.UsuarioDao;
import com.ejemplobase.demo.model.entity.Documento;
import com.ejemplobase.demo.model.entity.Usuario;
import com.ejemplobase.demo.service.interfaces.IDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentoService implements IDocumentoService {
    @Autowired
    private DocumentoDao documentoDao;

    @Autowired
    private UsuarioDao usuarioDao;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private final ResourceLoader resourceLoader;

    public DocumentoService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Documento registrarDocumento(MultipartFile file, String tipo, Integer usuarioid) {

        // Generar un nombre único para el archivo
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        /*
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
        }*/
        String rutadownload="";
        try{

            String absolutePath = resourceLoader.getResource(uploadPath).getFile().getAbsolutePath();

            // Ruta completa donde se guardará el archivo
            Path filePath = Paths.get(absolutePath + File.separator +  fileName);

            // Guardar el archivo en el sistema de archivos
            Files.write(filePath, file.getBytes());

            // Construir la URL para acceder al archivo cargado
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/files/")
                    .path(fileName)
                    .toUriString();

            rutadownload=fileDownloadUri;

        }catch (IOException e) {
            return null;
        }

        Usuario usuario=usuarioDao.findById(usuarioid).orElse(null);
        if(usuario==null){
            return null;
        }

        Documento doc=Documento.builder()
                .tipo(tipo)
                .ruta(rutadownload)
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

    @Override
    public Documento obtenerDocumento(Integer documetoid) {
        return this.documentoDao.findById(documetoid).orElse(null);
    }
}
