package com.domiaffaire.api.entities;

import com.domiaffaire.api.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String pwd;
    private String image;
    private UserRole userRole;
    private boolean isEnabled = false;
    private boolean isArchived = true;

    public User(UserDetails userDetails) {
        this.email = userDetails.getUsername();
        this.pwd = userDetails.getPassword();
        this.isEnabled = userDetails.isEnabled();
        this.isArchived = userDetails.isAccountNonLocked();
        this.userRole = UserRole.valueOf(userDetails.getAuthorities().iterator().next().getAuthority());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isArchived;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }


}
