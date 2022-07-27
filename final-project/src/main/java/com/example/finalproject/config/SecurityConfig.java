package com.example.finalproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception
//    {
//        http.csrf().disable();
//        http.cors().and().authorizeRequests().anyRequest().authenticated().and().oauth2ResourceServer().jwt();
//    }

//    @Bean
//    JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties, @Value("${auth0.audience}") String audience) {
//    /*
//        By default, Spring Security does not validate the "aud" claim of the token, to ensure that this token is
//        indeed intended for our app. Adding our own validator is easy to do:
//        */    String issuerUri = properties.getJwt().getIssuerUri();
//        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuerUri);
//        OAuth2TokenValidator<Jwt> audienceValidator = AudienceValidator.of(audience);
//        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
//        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);    jwtDecoder.setJwtValidator(withAudience);    return jwtDecoder;
//    }
//}

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt();

        return http.build();
    }
}

