package com.sneakysquid.authenticator.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private boolean locked;

    @Column(nullable = false)
    private boolean expired;

    @Column(name = "credentials_expired", nullable = false)
    private boolean credentialsExpired;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "activation_token")
    private String activationToken;

    @Column(name = "activation_token_expiry")
    private LocalDateTime activationTokenExpiry;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> groups;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return groups.stream()
                .flatMap(group -> group.getRoles().stream())
                .flatMap(role -> {
                    Set<GrantedAuthority> authorities = role.getAuthorities().stream()
                            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                            .collect(Collectors.toSet());
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                    return authorities.stream();
                })
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void updateLastLogin() {
        this.setLastLogin(LocalDateTime.now());
    }

    public void generateActivationToken() {
        this.activationToken = UUID.randomUUID().toString();
        this.activationTokenExpiry = LocalDateTime.now().plusHours(24);
    }
}
