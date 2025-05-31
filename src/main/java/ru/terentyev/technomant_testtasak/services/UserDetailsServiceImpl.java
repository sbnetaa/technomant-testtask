package ru.terentyev.technomant_testtasak.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.terentyev.technomant_testtasak.models.Person;
import ru.terentyev.technomant_testtasak.repositories.PersonRepository;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsManager {

    private final PersonRepository personRepository;

    @Autowired
    public UserDetailsServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = personRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = false)
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException("User already exists: " + user.getUsername());
        }
        personRepository.save((Person) user);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateUser(UserDetails user) {
        if (!userExists(user.getUsername())) {
            throw new IllegalArgumentException("User does not exist: " + user.getUsername());
        }
        personRepository.save((Person) user);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUser(String username) {
        personRepository.deleteByUsername(username);
    }

    @Override
    @Transactional(readOnly = false)
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Override
    public boolean userExists(String username) {
        return personRepository.existsByUsername(username);
    }
}
