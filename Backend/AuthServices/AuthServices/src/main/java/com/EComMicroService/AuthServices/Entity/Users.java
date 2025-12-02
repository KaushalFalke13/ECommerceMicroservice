package com.EComMicroService.AuthServices.Entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users  implements UserDetails{

    @Id
    private String id;
    private String email;
    private String password;
    private List<String> role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role.toString());
    }
    @Override
    public String getUsername() {
        return this.email;
    }
    
}
