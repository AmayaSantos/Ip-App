package meli.ipApp.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi api() {
    return GroupedOpenApi.builder()
        .group("IpApp")
        .packagesToScan("meli.ipApp.controller")
        .build();
  }


  @Bean
  public OpenAPI defineOpenApi() {
    Server server = new Server();
    server.setUrl("http://localhost:8080");
    server.setDescription("Development");

    Contact myContact = new Contact();
    myContact.setName("falsa 123");
    myContact.setEmail("not.publicmail@gmail.com");

    Info information = new Info()
        .title("IpAp Web")
        .version("1.0")
        .description("")
        .contact(myContact);
    return new OpenAPI()
        .info(information)
        .servers(List.of(server));
  }
}
