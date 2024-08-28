package com.yawka.xozcargo.repository;

import com.yawka.xozcargo.entity.Role;
import com.yawka.xozcargo.entity.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(Roles name);
}
