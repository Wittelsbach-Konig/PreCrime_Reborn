package ru.itmo.precrimeupd.security;

public class SecurityLiterals {
    public static final String AUTH_ENDPOINTS = "/api/v1/auth/**";
    public static final String[] ADMIN_ENDPOINTS = {
        "/api/v1/admin/**",
        "/api/v1/visions/add"
    };

    public static final String SWAGGER_ENDPOINTS = "/swagger-ui/**";
    public static final String APIDOCS_ENDPOINTS = "/v3/**";
    public static final String[] DETECTIVE_ENDPOINTS = {
            "/api/v1/cards/randomDateTime",
            "/api/v1/cards/randomVictimName",
            "/api/v1/cards/randomCriminalName",
            "/api/v1/cards/newcard",
            "/api/v1/cards/{id}",
            "/api/v1/cards/"
    };
    public static final String AUDITOR_ENDPOINTS = "/api/v1/auditor/**";
    public static final String[] REACT_GROUP_BOSS_ENDPOINTS = {
            "/api/v1/reactiongroup/**"
    };
    public static final String[] TECHNIC_ENDPOINTS = {
            "/api/v1/precogs/**",
            "/api/v1/visions/{id}/**"
    };
    public static final String[] COMMON_VISION_ENDPOINTS = {
            "/api/v1/visions",
            "/api/v1/visions/{id}"
    };
}
