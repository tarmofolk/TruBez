package com.tarmofolk.trubez.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, SALESMAN, BOOKER;

    @Override
    public String getAuthority() {
        return name();
    }

}
