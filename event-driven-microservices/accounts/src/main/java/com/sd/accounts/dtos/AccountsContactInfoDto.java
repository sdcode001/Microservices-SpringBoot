package com.sd.accounts.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;
import java.util.Map;


/**
 * The @ConfigurationProperties annotation enables binding of entire groups of application properties(from application.yaml)
 * with matching prefix to a bean. You define a configuration class with annotated field matching the properties, and SpringBoot
 * automatically maps the properties to the corresponding fields.
 * */
@ConfigurationProperties(prefix = "accounts")
@Getter @Setter
public class AccountsContactInfoDto {
   private String message;
   private Map<String,String> contactDetails;
   List<String> onCallSupport;
}
