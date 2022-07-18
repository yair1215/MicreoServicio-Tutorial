package com.tutorial.authservice.repository;


import com.tutorial.authservice.models.ERole;
import com.tutorial.authservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}