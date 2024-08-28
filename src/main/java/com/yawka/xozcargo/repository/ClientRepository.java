package com.yawka.xozcargo.repository;

import com.yawka.xozcargo.entity.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface ClientRepository extends CrudRepository<Client, Integer> {

    Client findByPhoneNumber(String phoneNumber);
}