package ru.terentyev.technomant_testtasak.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.terentyev.technomant_testtasak.models.Person;

import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    Person findByUsername(String username);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);
}
