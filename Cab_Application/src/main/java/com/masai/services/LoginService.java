package com.masai.services;


import com.masai.DTO.LoginDTO;
import com.masai.modelEntity.DriverSession;
import com.masai.modelEntity.CustomerSession;

public interface LoginService {

	

	public CustomerSession loginCustomer(LoginDTO customer);
	
	public DriverSession loginDriver(LoginDTO dto);

}
