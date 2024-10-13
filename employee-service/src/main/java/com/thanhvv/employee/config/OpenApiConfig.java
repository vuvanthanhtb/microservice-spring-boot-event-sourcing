package com.thanhvv.employee.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Employee API Document",
                description = "API documentation for Employee Service",
                version = "1.0",
                contact = @Contact(
                        name = "Vũ Văn Thanh",
                        email = "vuvanthanhtb@gmail.com"
                ),
                license = @License(
                        name = "MIT Licence",
                        url = "----"
                ),
                termsOfService = "------"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:9002"
                ),
                @Server(
                        description = "Dev ENV",
                        url = "http..."
                )
        }
)
public class OpenApiConfig {
}
