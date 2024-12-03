package com.restApi.RestAPI.repository;
import com.restApi.RestAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
