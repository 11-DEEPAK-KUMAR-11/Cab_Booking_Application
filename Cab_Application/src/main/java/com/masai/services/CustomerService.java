package com.masai.services;


import java.util.List;
import com.masai.DTO.CustomerDTO;
import com.masai.exceptions.CustomerException;
import com.masai.exceptions.TripException;
import com.masai.modelEntity.CompletedTrips;
import com.masai.modelEntity.Customer;
import com.masai.modelEntity.Driver;
import com.masai.modelEntity.TripBooking;


public interface CustomerService {

	public Customer register(Customer user) throws CustomerException;
	
	public List<Customer> getCustomer();
	
	public String deleteCustomer(CustomerDTO dto, String key) throws CustomerException;
	
	public Customer updateCustomer(Customer customer,String mobile, String key) throws CustomerException;
	
	public List<Driver> getAvailableDrivers();
	
	public TripBooking bookTrip(TripBooking trip, String key);  
	
	public CompletedTrips markJourneyCompleted(int tripId, String key, double payment );
	
	public String cancelTrip(String key, Integer tripId) throws TripException;
	
	public String logoutCustomer(String key);
	
}
