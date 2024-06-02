package com.domiaffaire.api.entities;

import com.domiaffaire.api.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Data
public class User implements UserDetails {
    @Id
    private String id;
    private String email;
    private String pwd;
    private byte[] image;
    private String name;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date birthDate;
    private String code;
    private UserRole userRole;
    private boolean isEnabled = false;
    private boolean isNotArchived = true;
    private int domiciliationNotificationCount = 0;



    @DBRef
    @JsonIgnore
    private List<ConsultationRequest> consultationRequests = new ArrayList<>();
    @DBRef
    @JsonIgnore
    private List<Blog> savedBlogs = new ArrayList<>();
    @DBRef
    @JsonIgnore
    private Set<Blog> archivedBlogs = new HashSet<>();
    @DBRef
    @JsonIgnore
    private List<Chat> userChats = new ArrayList<>();

    public User(UserDetails userDetails) {
        this.email = userDetails.getUsername();
        this.pwd = userDetails.getPassword();
        this.isEnabled = userDetails.isEnabled();
        this.isNotArchived = userDetails.isAccountNonLocked();
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
        return isNotArchived;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }


}
