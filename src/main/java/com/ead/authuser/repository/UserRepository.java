package com.ead.authuser.repository;

import com.ead.authuser.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)//Trazendo os dados que estao como Laze loader
    Optional<User> findByUserName (@Param("userName") String userName);

    Optional<User> findByEmail (@Param("email") String email);
}
