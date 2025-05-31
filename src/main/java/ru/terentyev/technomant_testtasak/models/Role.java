package ru.terentyev.technomant_testtasak.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Role extends AbstractEntity implements GrantedAuthority {

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

    public Role(){}
}
