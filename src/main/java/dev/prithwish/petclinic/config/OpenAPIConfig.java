package dev.prithwish.petclinic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Server URL in Local environment");

        Contact contact = new Contact();
        contact.setEmail("wprith@gmail.com");
        contact.setName("Prithwish Samanta");
        contact.setUrl("https://github.com/prithwish-samanta");

        Info info = new Info()
                .title("Pet Clinic API documentation")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage pet clinic.");

        return new OpenAPI().info(info).servers(List.of(localServer));
    }
}
