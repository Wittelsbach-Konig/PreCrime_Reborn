package ru.itmo.backend.security;

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

    private final JwtAuthEntryPoint authEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomUserDetailService userDetailService;
    @Autowired
    public WebSecurityConfig(CustomUserDetailService userDetailsService
            , JwtAuthEntryPoint authEntryPoint
            , CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.userDetailService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(SecurityLiterals.AUTH_ENDPOINTS).permitAll()
                .antMatchers(SecurityLiterals.ADMIN_ENDPOINTS).hasAnyAuthority("ADMIN")
                .antMatchers("/api/v1/me").authenticated()
                .antMatchers("/actuator").hasAuthority("ADMIN")
                .antMatchers(SecurityLiterals.SWAGGER_ENDPOINTS).permitAll()
                .antMatchers(SecurityLiterals.APIDOCS_ENDPOINTS).permitAll()
                .antMatchers(SecurityLiterals.AUDITOR_ENDPOINTS).hasAuthority("AUDITOR")
                .antMatchers(SecurityLiterals.DETECTIVE_ENDPOINTS).hasAuthority("DETECTIVE")
                .antMatchers(SecurityLiterals.TECHNIC_ENDPOINTS).hasAuthority("TECHNIC")
                .antMatchers(SecurityLiterals.REACT_GROUP_BOSS_ENDPOINTS).hasAuthority("REACTIONGROUP")
                .antMatchers(SecurityLiterals.COMMON_VISION_ENDPOINTS).hasAnyAuthority("ADMIN", "TECHNIC")
                .anyRequest()
                .authenticated()
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .formLogin().disable()
                .logout().disable()
                .httpBasic();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtRequestFilter jwtAuthenticationFilter() {
        return new JwtRequestFilter();
    }
}
