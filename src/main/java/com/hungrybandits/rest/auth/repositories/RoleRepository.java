package com.hungrybandits.rest.auth.repositories;

import com.hungrybandits.rest.auth.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthority(String authority);
}
