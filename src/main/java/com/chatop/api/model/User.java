package com.chatop.api.model;

import java.util.Collection;
import java.util.Date;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String password;

    @Column(name="created_at")
    private Date createdAt;

    @Column(name="updated_at")
    private Date updatedAt;

    // Implémentation des méthodes de UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retournez les autorisations/roles de l'utilisateur
        return null;
    }

    @Override
    public String getPassword() {
        // Retournez le mot de passe de l'utilisateur
        return this.password;
    }

    @Override
    public String getUsername() {
        // Retournez le nom d'utilisateur de l'utilisateur
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Retournez true si le compte n'a pas expiré
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Retournez true si le compte n'est pas verrouillé
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Retournez true si les informations d'identification ne sont pas expirées
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Retournez true si le compte est activé
        return true;
    }
}
