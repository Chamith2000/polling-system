package com.oexil.univote.repository;

import com.oexil.univote.enums.ERole;
import com.oexil.univote.model.masters.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
