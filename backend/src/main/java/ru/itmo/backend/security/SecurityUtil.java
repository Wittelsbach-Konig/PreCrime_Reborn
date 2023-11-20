package ru.itmo.backend.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityUtil {
    public String getSessionUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            return authentication.getName();
        }
        return null;
    }


    public List<String> getSessionUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority authority : authorities) {
                roles.add(authority.getAuthority());
            }
            return roles;
        }
        return Collections.emptyList();
    }
}
