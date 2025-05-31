package ru.terentyev.technomant_testtasak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.terentyev.technomant_testtasak.models.Role;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByName(String name);
    Role findByName(String roleUser);
}
