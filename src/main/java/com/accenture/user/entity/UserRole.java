package com.accenture.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_role")
public class UserRole implements Serializable, GrantedAuthority {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @JsonIgnore
    Long id;
    @Column(name = "name")
    String name;

    public UserRole() {
    }

    public UserRole(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserRole(String name) {
        String role = name.toUpperCase();
        switch (role) {
            case "ADMIN":
                this.id = 1L;
                break;
            case "AGENT":
                this.id = 2L;
                break;
            case "CUSTOMER":
                this.id = 3L;
                break;
            default:
                this.id = null;
        }
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(getId(), userRole.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }
}
