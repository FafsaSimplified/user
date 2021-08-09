package com.accenture.user.dao;

import com.accenture.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = "FROM User u WHERE u.username = :usernameOrEmailOrPhone OR " +
            "u.email = :usernameOrEmailOrPhone OR " +
            "u.cellPhone = :usernameOrEmailOrPhone")
    Optional<User> findByUsernameOrEmailOrPhone(@Param("usernameOrEmailOrPhone") String usernameOrEmailOrPhone);
}
