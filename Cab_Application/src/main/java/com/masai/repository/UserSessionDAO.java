package com.masai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.masai.modelEntity.CustomerSession;


@Repository
public interface UserSessionDAO extends JpaRepository<CustomerSession, Integer> {

	public Optional<CustomerSession> findByUserId(Integer userId);
	
	public Optional<CustomerSession> findByUuid(String  uuid);

}
