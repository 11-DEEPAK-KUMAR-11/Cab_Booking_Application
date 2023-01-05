package com.masai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.masai.modelEntity.Customer;

@Repository
public interface CustomerDAO extends JpaRepository<Customer, Integer>{

	
	Optional<Customer> findByUserMobile(String mobile);
}
