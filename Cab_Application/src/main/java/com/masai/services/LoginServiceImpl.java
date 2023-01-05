package com.masai.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.masai.DTO.LoginDTO;
import com.masai.exceptions.CustomerException;
import com.masai.exceptions.LoginException;
import com.masai.modelEntity.Customer;
import com.masai.modelEntity.Driver;
import com.masai.modelEntity.DriverSession;
import com.masai.modelEntity.CustomerSession;
import com.masai.repository.CustomerDAO;
import com.masai.repository.DriverDAO;
import com.masai.repository.DriverSessionDao;
import com.masai.repository.UserSessionDAO;

@Service
public class LoginServiceImpl implements LoginService {
	

	@Autowired
	private UserSessionDAO sessionDAO;

	@Autowired
	private CustomerDAO customerDAO;


	@Autowired
	DriverSessionDao driverSessionDao;

	@Autowired
	DriverDAO driverDAO;

	@Override
	public CustomerSession loginCustomer(LoginDTO customer) {

		Optional<Customer> res = customerDAO.findByUserMobile(customer.getMobile());

		if (!res.isPresent()) {
			System.out.println(res + " Data is empty");
			throw new CustomerException("Customer does not exist with the given mobile number");
		}

		Customer existingCustomer = res.get();
		Optional<CustomerSession> opt = sessionDAO.findByUserId(existingCustomer.getCustomerId());

		if (opt.isPresent())
			throw new LoginException("User already logged in");

		if (existingCustomer.getUser().getPassword().equals(customer.getPassword())) {

			CustomerSession newSession = new CustomerSession();

			newSession.setUserId(existingCustomer.getCustomerId());
			newSession.setUserType("Customer");
			newSession.setSessionStartTime(LocalDateTime.now());
			newSession.setSessionEndTime(LocalDateTime.now().plusHours(1));

			UUID uuid = UUID.randomUUID();
			String token = uuid.toString().split("-")[0];

			newSession.setUuid(token);

			return sessionDAO.save(newSession);
		} else {
			throw new LoginException("Password Incorrect. Try again.");
		}

	}


	@Override
	public DriverSession loginDriver(LoginDTO driver) {
		Optional<Driver> res = driverDAO.findByUserMobile(driver.getMobile());

		if (!res.isPresent()) {
			System.out.println(res + " Data is empty");
			throw new CustomerException("Driver does not exist with the given mobile number");
		}

		Driver existingDriver = res.get();
		Optional<DriverSession> opt = driverSessionDao.findByDriverId(existingDriver.getDriverId());

		if (opt.isPresent())
			throw new LoginException("User already logged in");

		if (existingDriver.getUser().getPassword().equals(driver.getPassword())) {

			DriverSession newSession = new DriverSession();

			newSession.setDriverId(existingDriver.getDriverId());
			newSession.setUserType("Driver");
			newSession.setSessionStartTime(LocalDateTime.now());
			newSession.setSessionEndTime(LocalDateTime.now().plusHours(1));

			UUID uuid = UUID.randomUUID();
			String token = uuid.toString().split("-")[0];

			newSession.setUuid(token);

			return driverSessionDao.save(newSession);
		} else {
			throw new LoginException("Password Incorrect. Try again.");
		}
	}

}
