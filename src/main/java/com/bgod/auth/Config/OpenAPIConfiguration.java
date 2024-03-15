package com.bgod.auth.Config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
//swagger ekliyoruz

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI defineOpenApi(){
        Server server=new Server();
        server.setUrl("http://localhost:8000");
        server.setDescription("Development");
        Info info =new Info()
                .title("BGod")
                .version("1.0")
                .description("example of auth and CRUD for userService");
        final String security="bearerAuth";

        return new OpenAPI().info(info).servers(List.of(server))
                .components( new Components()
                        .addSecuritySchemes(
                                security,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("jwt")))
                .security(List.of(new SecurityRequirement().addList(security)))
                .info(info)
                ;
    }


}
