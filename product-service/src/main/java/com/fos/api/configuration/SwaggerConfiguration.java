package com.fos.api.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openApiInformation() {
        log.info("Swagger URL: http://localhost:8080/swagger-ui.html");
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Localhost Server URL");

        Contact contact = new Contact()
                .email("pasindu.gunawardana@sysco.com")
                .name("Pasindu Gunawardana");

        Info info = new Info()
                .contact(contact)
                .description("Spring Boot 3 + Open API 3")
                .summary("This is API endpoints for Product Service")
                .title("Mini Ecommerce Web App API")
                .version("V1.0.0")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        return new OpenAPI().info(info).addServersItem(localServer);
    }
}
