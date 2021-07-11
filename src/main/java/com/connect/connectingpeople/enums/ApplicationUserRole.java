package com.connect.connectingpeople.enums;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.connect.connectingpeople.enums.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    USER(Sets.newHashSet(PROFILE_READ)),
    ADMIN(Sets.newHashSet(PROFILE_READ, PROFILE_WRITE, PROFILE_UPDATE, PROFILE_DELETE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions(){
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        var permissions = getPermissions()
                .stream().map(authority -> new SimpleGrantedAuthority(authority.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissions;
    }
}
