package com.bbva.minibank.application.repository;

import com.bbva.minibank.infrastructure.entities.UserEntity;

import java.util.Optional;

public interface IUserRepository {
	Optional<UserEntity> findByUsername(String username);
}
