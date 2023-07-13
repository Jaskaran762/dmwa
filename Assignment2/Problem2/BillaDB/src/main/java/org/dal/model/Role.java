package org.dal.model;

import java.util.List;

public class Role {

    private String roleName;
    private List<String> authorities;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return roleName + ", " + authorities.toString();
    }
}
