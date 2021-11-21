package com.mindata.superheroes.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum Role {
    VIEW(Arrays.asList(Permission.VIEW)),
    ADMIN(Arrays.asList(Permission.values()));

    private final List<Permission> permissions;

}
