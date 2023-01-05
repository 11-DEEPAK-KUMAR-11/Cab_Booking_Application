package com.masai.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.masai.DTO.CustomerDTO;
import com.masai.DTO.LoginDTO;
import com.masai.exceptions.CustomerException;
import com.masai.modelEntity.CompletedTrips;
import com.masai.modelEntity.Customer;
import com.masai.modelEntity.Driver;
import com.masai.modelEntity.TripBooking;
import com.masai.modelEntity.CustomerSession;
import com.masai.services.CustomerService;
import com.masai.services.LoginService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@Autowired
	private LoginService loginService;

	@PostMapping("/login")
	public ResponseEntity<CustomerSession> loginCustomer(@RequestBody LoginDTO customerdto) {
		return new ResponseEntity<>(loginService.loginCustomer(customerdto), HttpStatus.ACCEPTED);
	}

	@PostMapping("/register")
	public Customer registerCustomer(@RequestBody Customer user) {
		Customer newUser = customerService.register(user);
		return newUser;

	}

	@GetMapping("/customerlist")
	public List<Customer> getAllCustomer() {
		List<Customer> list = customerService.getCustomer();
		if (list.isEmpty())
			throw new CustomerException("There is no customer found in the database.");
		return list;
	}

	@DeleteMapping("/delete")
	public String deleteCustomer(@RequestBody CustomerDTO dto, @RequestParam String key) {
		return customerService.deleteCustomer(dto, key);

	}

	@PutMapping("/update/{mobile}")
	public Customer updateCustomerByMobile(@RequestBody Customer customer, @PathVariable("mobile") String mobile,
			@RequestParam String key) {
		return customerService.updateCustomer(customer, mobile, key);
	}

	@GetMapping("/availablecabs")
	public List<Driver> availableCabs() {

		return customerService.getAvailableDrivers();

	}

	@PostMapping("/booktrip")
	public ResponseEntity<TripBooking> bookTrip(@RequestBody TripBooking trip, @RequestParam String key) {

		TripBooking bookedTrip = customerService.bookTrip(trip, key);

		ResponseEntity<TripBooking> confirmed = new ResponseEntity<TripBooking>(bookedTrip, HttpStatus.CREATED);

		return confirmed;
	}
	
	@GetMapping("/markJourneyCompleted")
	public CompletedTrips markJourneyCompleted(@RequestParam Integer tripId,@RequestParam String key, @RequestParam double payment) {

		return customerService.markJourneyCompleted(tripId, key,payment);
	}

	@GetMapping("/logout")
	public String logoutCustomer(@RequestParam String key) {

		return customerService.logoutCustomer(key);
	}


	@DeleteMapping("/canceltrip")
	public String cancelTrip(@RequestParam String key, @RequestParam Integer tripId) {
		return customerService.cancelTrip(key, tripId);
	}

	
}
