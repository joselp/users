package com.jp.dev.users.infrastructure.security;

import static com.jp.dev.commons.security.SecurityConstants.AUTH_URL;
import static com.jp.dev.commons.security.SecurityConstants.SIGN_UP_URL;

import com.jp.dev.commons.security.AuthenticationEntryPointCustom;
import com.jp.dev.commons.security.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final UserDetailsService userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final AuthenticationManager authenticationManager;

  public SecurityConfig(
      @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      @Lazy AuthenticationManager authenticationManager) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.authenticationManager = authenticationManager;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, SIGN_UP_URL, AUTH_URL)
                    .permitAll()
                    .requestMatchers("/h2-console/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilter(new JWTAuthenticationFilter(authenticationManager))
        .addFilter(new JWTAuthorizationFilter(authenticationManager))
        .sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.headers(hd -> hd.frameOptions(FrameOptionsConfig::disable));

    http.httpBasic(
        httpBasic -> httpBasic.authenticationEntryPoint(new AuthenticationEntryPointCustom()));

    http.cors(
        cors -> {
          CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
          corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
          corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);
          corsConfiguration.addAllowedMethod(HttpMethod.PATCH);
          corsConfiguration.addAllowedMethod(HttpMethod.PUT);
          corsConfiguration.addAllowedMethod(HttpMethod.POST);
          final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/**", corsConfiguration);
          cors.configurationSource(source);
        });

    http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsService)
        .passwordEncoder(bCryptPasswordEncoder);

    return http.build();
  }
}
