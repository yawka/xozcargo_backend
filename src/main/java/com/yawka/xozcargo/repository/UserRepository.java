package com.yawka.xozcargo.repository;

import com.yawka.xozcargo.entity.Client;
import com.yawka.xozcargo.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByUsername(String name);
    Optional<User> findByClient(Client client);

}
