package daw.gestiongastos;

import daw.gestiongastos.servicio.IStorageServicio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class) // <-- Habilitar la clase de propiedades
public class GestiongastosApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestiongastosApplication.class, args);
	}

	// Bean para inicializar el StorageService en el arranque
	@Bean
	CommandLineRunner init(IStorageServicio storageServicio) {
		return (args) -> {
			storageServicio.init();
		};
	}
}
