package com.admin.security;

import com.storage.admin.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomAdminDetails implements UserDetails {

    private final Admin admin;
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ROLE_ADMIN));
    }

    @Override
    public String getPassword() {
        return admin.getAccount().getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getAccount().getEmail();
    }

    public Admin getAdmin() {
        return admin;
    }

}

