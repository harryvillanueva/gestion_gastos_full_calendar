package daw.gestiongastos.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServicio {

    @Autowired
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String remitente;


    private static final String DESTINATARIO = "harry.villanueva.gallardo@gmail.com";


    public void enviarNotificacionBalanceBajo(double balanceActual) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(DESTINATARIO);
            mensaje.setSubject("Alerta de Balance Bajo - Esta usted en balance negativo");

            String texto = String.format(
                    "¡Hola!\n\nTu balance en el sistema de gestión de gastos es ahora de: %.2f €.\n\n" +
                            "Estás por debajo de 0.00 €. Ingrese Para quedar en positivo.\n\n" +
                            "Saludos,\nTu aplicación de Banco.",
                    balanceActual
            );

            mensaje.setText(texto);
            mailSender.send(mensaje);
            System.out.println("Correo de alerta enviado exitosamente.");
        } catch (Exception e) {
            // Manejar la excepción (por ejemplo, loguearla)
            System.err.println("Error al enviar el correo de alerta: " + e.getMessage());
        }
    }
}
