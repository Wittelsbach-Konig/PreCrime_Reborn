package ru.itmo.precrimeupd.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private CustomUserDetailService userDetailService;

    @Autowired
    public WebSecurityConfig(CustomUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    private static final String[] ENDPOINTS_WHITELIST = {
            "/css/**",
            "/image/**",
            "/js/**",
            "/includes/**",
            "/registration",
            "/login",
            "/error",
    };
    private static final String [] DETECTIVE_WHITELIST = {
            "/investigation",
            "/cardfill"
    };

    private static final String ADMIN_URL = "/admin";

    public static final String LOGIN_URL = "/login";
    public static final String LOGIN_PROCESSING_URL = "/process_login";
    public static final String DEFAULT_SUCCESS_URL = "/cabinet";
    public static final String LOGOUT_URL = "/logout";
    public static final String LOGIN_FAIL_URL = LOGIN_URL + "?error";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests((requests) -> requests
                        .antMatchers(ENDPOINTS_WHITELIST).permitAll()
                        .antMatchers(ADMIN_URL).hasRole("ADMIN")
                        .antMatchers(DETECTIVE_WHITELIST).hasRole("DETECTIVE")
                        .anyRequest()
                        .authenticated()
                )
                .formLogin((form) -> form
                        .loginPage(LOGIN_URL)
                        .loginProcessingUrl(LOGIN_PROCESSING_URL)
                        .defaultSuccessUrl(DEFAULT_SUCCESS_URL)
                        .failureUrl(LOGIN_FAIL_URL)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl(LOGOUT_URL)
                        .logoutSuccessUrl(LOGIN_URL)
                        .permitAll());
        return http.build();
    }

    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

}
