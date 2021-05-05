package org.vaadin.crudui.demo.service;

import org.springframework.stereotype.Service;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    public static final int USERS_COUNT_LIMIT = 1000;

    public static class LimitReached extends RuntimeException {
    }

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public int countAll() {
        return (int) userRepository.count();
    }

    public List<User> findByNameContainingIgnoreCase(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    public User save(User user) {
        if (countAll() >= USERS_COUNT_LIMIT) {
            throw new LimitReached();
        }

        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

}
