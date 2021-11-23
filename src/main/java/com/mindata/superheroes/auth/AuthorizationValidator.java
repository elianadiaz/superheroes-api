package com.mindata.superheroes.auth;

import com.mindata.superheroes.exceptions.AuthorizationException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public final class AuthorizationValidator {

    private AuthorizationValidator() {}

    private static final String TOKEN_USER_ADMIN_ROLE = "TEST_ADMIN_ROLE";
    private static final String TOKEN_USER_VIEW_ROLE = "TEST_VIEW_ROLE";

    private static final Map<String, Role> VALID_USERS;
    static {
        // Immutable collection
        VALID_USERS = Map.of(TOKEN_USER_ADMIN_ROLE, Role.ADMIN, TOKEN_USER_VIEW_ROLE, Role.VIEW);
    }

    public static void validateUser(final String token, final Permission permission) throws AuthorizationException {
        if (StringUtils.isEmpty(token)) {
            throw AuthorizationException.ofMissingToken();
        }

        final Role role = VALID_USERS.get(token);
        if (role == null) {
            throw AuthorizationException.ofUnauthorized();
        }

        role.getPermissions()
            .stream()
            .filter(p -> p == permission)
            .findAny()
            .orElseThrow(() -> AuthorizationException.ofForbidden());
    }

}
