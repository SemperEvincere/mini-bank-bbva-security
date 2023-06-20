package com.bbva.minibank.infrastructure.repositories.springdatajpa;

import com.bbva.minibank.infrastructure.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserSpringRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsername(String username);
}
