package com.bbva.minibank.infrastructure.repositories;

import com.bbva.minibank.application.repository.IUserRepository;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.infrastructure.repositories.springdatajpa.IUserSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements IUserRepository {
	
	private final IUserSpringRepository userSpringRepository;
	@Override
	public Optional<UserEntity> findByUsername(String username) {
		return userSpringRepository.findByUsername(username);
	}
}
