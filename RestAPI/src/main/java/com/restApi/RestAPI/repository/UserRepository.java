package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.auth.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Page<Users> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}
