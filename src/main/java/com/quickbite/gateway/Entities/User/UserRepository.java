package com.quickbite.gateway.Entities.User;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    @NonNull
    List<User> findAll();

    Optional<User> findByEmail(String email);
}
