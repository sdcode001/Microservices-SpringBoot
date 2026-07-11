package com.sd.accounts;

import com.sd.accounts.dtos.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
/**
 * Enables and registers the AccountsContactInfoDto configuration bean.
 * Binds application properties starting with its prefix into a type-safe Java object.
 */
@EnableConfigurationProperties(AccountsContactInfoDto.class)
@OpenAPIDefinition(
        info = @Info(
                title = "Accounts microservice REST API Documentation",
                description = "EazyBank Accounts microservice REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Souvik Dey",
                        email = "deysouvik620@gmail.com",
                        url = "https://www.github.com/sdcode001"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.github.com/sdcode001"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description =  "EazyBank Accounts microservice REST API Documentation",
                url = "https://https://www.github.com/sdcode001/swagger-ui.html"
        )
)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
