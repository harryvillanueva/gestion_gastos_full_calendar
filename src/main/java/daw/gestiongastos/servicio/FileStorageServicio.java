package daw.gestiongastos.servicio; // <-- Paquete de servicio

import daw.gestiongastos.StorageProperties; // <-- Import del paquete raíz
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.net.MalformedURLException;

@Service
public class FileStorageServicio implements IStorageServicio {

    private final Path rootLocation;

    @Autowired
    public FileStorageServicio(StorageProperties properties) {

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el almacenamiento de archivos", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Falló al guardar un fichero vacío.");
            }


            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            Path destinationFile = this.rootLocation.resolve(Paths.get(uniqueFilename))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {

                throw new RuntimeException("No se puede guardar el fichero fuera del directorio actual.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

                return uniqueFilename;
            }
        } catch (IOException e) {
            throw new RuntimeException("Falló al guardar el fichero.", e);
        }

    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer el fichero: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error en la URL del fichero: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {

        if (filename == null || filename.isEmpty()) {
            return;
        }

        try {
            Path file = rootLocation.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {

            System.err.println("No se pudo borrar el fichero: " + filename + ". Razón: " + e.getMessage());
        }
    }



}
