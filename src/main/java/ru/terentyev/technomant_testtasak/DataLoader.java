package ru.terentyev.technomant_testtasak;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.terentyev.technomant_testtasak.models.Role;
import ru.terentyev.technomant_testtasak.repositories.RoleRepository;

@Component
public class DataLoader {

    private final RoleRepository roleRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void createRoles(){
        if (!roleRepository.existsByName("ROLE_USER")) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
    }
}
