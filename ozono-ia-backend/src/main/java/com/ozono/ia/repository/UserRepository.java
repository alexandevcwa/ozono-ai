package com.ozono.ia.repository;

import com.ozono.ia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("""
            select u from User u where u.email = :email
            """
    )
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("select u.id from User u where u.username = :username")
    Integer findIdByUsername(String username);

    @Query("select u.credit from User u where u.username = :username")
    Integer findCreditByUsername(String username);

    @Modifying
    @Transactional
    @Query("update User u set u.credit = u.credit - 1 where u.username = :username")
    void decreaseCreditByUsername(String username);


}
