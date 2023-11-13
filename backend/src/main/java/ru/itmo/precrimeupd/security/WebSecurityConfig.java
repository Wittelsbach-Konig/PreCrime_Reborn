package ru.itmo.precrimeupd.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomUserDetailService userDetailService;
    private final JwtAuthEntryPoint authEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public WebSecurityConfig(CustomUserDetailService userDetailService, JwtAuthEntryPoint authEntryPoint, JwtRequestFilter jwtRequestFilter) {
        this.userDetailService = userDetailService;
        this.authEntryPoint = authEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(SecurityLiterals.AUTH_ENDPOINTS).permitAll()
                .antMatchers(SecurityLiterals.ADMIN_ENDPOINTS).hasAnyAuthority("ADMIN")
                .antMatchers("/api/v1/me").hasAuthority("ADMIN")
                .antMatchers(SecurityLiterals.SWAGGER_ENDPOINTS).permitAll()
                .antMatchers(SecurityLiterals.APIDOCS_ENDPOINTS).permitAll()
                .antMatchers(SecurityLiterals.AUDITOR_ENDPOINTS).hasAuthority("AUDITOR")
                .antMatchers(SecurityLiterals.DETECTIVE_ENDPOINTS).hasAuthority("DETECTIVE")
                .antMatchers(SecurityLiterals.TECHNIC_ENDPOINTS).hasAuthority("TECHNIC")
                .antMatchers(SecurityLiterals.COMMON_VISION_ENDPOINTS).hasAnyAuthority("ADMIN", "TECHNIC")
                .antMatchers(SecurityLiterals.COMMON_CARDS_ENDPOINTS).hasAnyAuthority("DETECTIVE", "AUDITOR")
                .anyRequest()
                .authenticated()
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin().disable()
                .logout().disable()
                .httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
