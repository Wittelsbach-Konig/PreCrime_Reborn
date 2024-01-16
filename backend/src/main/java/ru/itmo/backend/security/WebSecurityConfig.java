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
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthEntryPoint authEntryPoint;
    private final CustomUserDetailService userDetailService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

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
                .cors().and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/index.html", "/static/**",
                        "/*.ico", "/*.json", "/*.png").permitAll()
                .antMatchers(SecurityLiterals.AUTH_ENDPOINTS).anonymous()
                .antMatchers("/api/v1/credits").permitAll()
                .antMatchers(SecurityLiterals.ADMIN_ENDPOINTS).hasAuthority("ADMIN")
                .antMatchers("/api/v1/me").authenticated()
                .antMatchers(SecurityLiterals.SWAGGER_ENDPOINTS).permitAll()
                .antMatchers(SecurityLiterals.APIDOCS_ENDPOINTS).permitAll()
                .antMatchers(SecurityLiterals.AUDITOR_ENDPOINTS).hasAuthority("AUDITOR")
                .antMatchers(SecurityLiterals.DETECTIVE_ENDPOINTS).hasAuthority("DETECTIVE")
                .antMatchers(SecurityLiterals.TECHNIC_ENDPOINTS).hasAuthority("TECHNIC")
                .antMatchers(SecurityLiterals.REACT_GROUP_BOSS_ENDPOINTS).hasAuthority("REACTIONGROUP")
                .antMatchers(SecurityLiterals.TECHNIC_DETECTIVE_VISION_ENDPOINTS).hasAnyAuthority("TECHNIC", "DETECTIVE")
                .antMatchers(SecurityLiterals.COMMON_VISION_ENDPOINTS).hasAnyAuthority("ADMIN", "TECHNIC")
                .anyRequest()
                .authenticated()
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .formLogin().disable()
                .logout().disable()
                .httpBasic();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(spaWebFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "X-Api-Key", "X-Requested-With", "Content-Type", "Accept", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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

    @Bean
    public SpaWebFilter spaWebFilter() { return new SpaWebFilter();}

    @Bean
    public RequestCache referrerRequestCache() {
        return new HttpSessionRequestCache() {
            @Override
            public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
                String referrer = request.getHeader("referrer");
                if (referrer == null) {
                    referrer = request.getRequestURL().toString();
                }
                request.getSession().setAttribute("SPRING_SECURITY_SAVED_REQUEST",
                        new SimpleSavedRequest(referrer));
            }
        };

    }
}
