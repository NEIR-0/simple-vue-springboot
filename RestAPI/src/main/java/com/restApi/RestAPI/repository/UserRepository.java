// file ini fungsinya untuk query manual
package com.restApi.RestAPI.repository;
import com.restApi.RestAPI.interfaceList.UserDTO;
import com.restApi.RestAPI.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u ORDER BY LOWER(u.name) DESC")
    List<User> findAllUsersSortedByName();

    @Query("SELECT u FROM User u")
    @EntityGraph(attributePaths = {"jobs", "card"})
    List<User> findAllWithJobsAndCard();
}