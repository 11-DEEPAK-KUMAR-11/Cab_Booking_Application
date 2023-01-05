package com.masai.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.math.RoundingMode;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.masai.exceptions.CustomerException;
import com.masai.exceptions.TripException;
import com.masai.modelEntity.CompletedTrips;
import com.masai.modelEntity.Customer;
import com.masai.modelEntity.Driver;
import com.masai.repository.CompletedTripsDao;
import com.masai.repository.CustomerDAO;
import com.masai.repository.DriverDAO;
import com.masai.repository.TripbookingDao;
import com.masai.DTO.CustomerDTO;
import com.masai.modelEntity.TripBooking;
import com.masai.modelEntity.TripStatus;
import com.masai.modelEntity.CustomerSession;
import com.masai.repository.UserSessionDAO;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	UserSessionDAO userSessionDao;

	@Autowired
	DriverDAO driverDao;

	@Autowired
	TripbookingDao tripBookingDao;

	@Autowired
	CompletedTripsDao completedTripsDao;

	@Override
	public Customer register(Customer user) {

		Optional<Customer> existing = customerDAO.findByUserMobile(user.getUser().getMobile());

		if (existing.isPresent()) {

			System.out.println("Customer is already present");
			throw new CustomerException("A Customer already exist with this mobile number in the Database");
		}

		return customerDAO.save(user);

	}

	@Override
	public List<Customer> getCustomer() {

		List<Customer> list = customerDAO.findAll();

		return list;
	}


	@Override
	public String deleteCustomer(CustomerDTO dto, String key) {

		Optional<CustomerSession> otp = userSessionDao.findByUuid(key);
		if (otp.isEmpty())
			throw new CustomerException("User is not logged in, Please login first!");
		else {
			Optional<Customer> opt = customerDAO.findByUserMobile(dto.getMobile());
			if (opt.isEmpty())
				throw new CustomerException("Username not found");
			else {
				Customer toBeDelete = opt.get();
				customerDAO.delete(toBeDelete);
			}
		}
		return "Your Id with Username " + dto.getMobile() + " is Deleted.";
	}

	@Override
	public Customer updateCustomer(Customer customer, String mobile, String key) {

		Customer updated = null;

		Optional<CustomerSession> otp = userSessionDao.findByUuid(key);

		if (otp.isEmpty())
			throw new CustomerException("User is not logged in, Please login first!");
		else {
			Optional<Customer> opt = customerDAO.findByUserMobile(mobile);
			if (opt.isEmpty())
				throw new CustomerException("Username not found");
			else {
				Customer toUpdate = opt.get();
				Integer id = toUpdate.getCustomerId();
				Customer newOne = new Customer(customer.getUser(), id);
				updated = customerDAO.save(newOne);
			}
		}
		return updated;
	}

	@Override
	public List<Driver> getAvailableDrivers() {

		List<Driver> listOfAvailableDrivers = driverDao.findByCabAvailable("YES");

		return listOfAvailableDrivers;
	}

	@Override
	public TripBooking bookTrip(TripBooking trip, String key) {
		TripBooking res = null;
		Random r = new Random();
		Double doubleKM = r.nextDouble(10, 500);
		BigDecimal bigD = new BigDecimal(doubleKM).setScale(2, RoundingMode.HALF_UP);
		Double distanceKm = bigD.doubleValue();

		Optional<CustomerSession> otp = userSessionDao.findByUuid(key);
		if (otp.isEmpty())
			throw new CustomerException("User is not logged in, Please login first!");
		else {
			Optional<Customer> cust = customerDAO.findById(trip.getCustomer().getCustomerId());
			Customer checkCustomer = cust.get();

			Optional<Driver> driv = driverDao.findById(trip.getDriver().getDriverId());
			Driver checkDriver = driv.get();
			
			TripBooking newTrip = new TripBooking();
			newTrip.setCustomer(checkCustomer);
			newTrip.setDistanceInKm(distanceKm);
			
			Double billAmount = checkDriver.getCab().getCabType().getPrice() * newTrip.getDistanceInKm();
			BigDecimal bigDec = new BigDecimal(billAmount).setScale(2, RoundingMode.HALF_UP);
			Double roundBill = bigDec.doubleValue();
			newTrip.setBill(roundBill);
			
			newTrip.setDriver(checkDriver);
			newTrip.setFromDate(trip.getFromDate());
			newTrip.setFromLocation(trip.getFromLocation());
			newTrip.setStatus(TripStatus.CONFIRMED);
			newTrip.setToDate(trip.getToDate());
			newTrip.setToLocation(trip.getToLocation());
			res = tripBookingDao.save(newTrip);

		}
		return res;

	}

	@Override
	public String cancelTrip(String key, Integer tripId) {
		Optional<CustomerSession> otp = userSessionDao.findByUuid(key);
		if (otp.isEmpty())
			throw new CustomerException("User is not logged in, Please login first!");
		Optional<TripBooking> opt = tripBookingDao.findById(tripId);
		if (opt.isEmpty())
			throw new TripException("No trips found by this TripId");
		TripBooking oldTrip = opt.get();
		tripBookingDao.deleteById(oldTrip.getTripbookingId());
		return "Your Trip is Cancelled Successfully!";

	}

	@Override
	public String logoutCustomer(String key) {
		Optional<CustomerSession> otp = userSessionDao.findByUuid(key);
		if (otp.isEmpty())
			throw new CustomerException("User is not logged in, Please login first!");
		else {

			userSessionDao.delete(otp.get());
		}

		return "Customer has succefully logged out.";

	}

	@Override
	public CompletedTrips markJourneyCompleted(int tripId, String key, double payment) {

		Optional<CustomerSession> otp = userSessionDao.findByUuid(key);
		if (otp.isEmpty())
			throw new CustomerException("User is not logged in, Please login first!");
		Optional<TripBooking> opt = tripBookingDao.findById(tripId);
		if (opt.isEmpty())
			throw new TripException("No trips found by this TripId");
		TripBooking oldTrip = opt.get();

		CompletedTrips completed=new CompletedTrips();
		Random randomNum = new Random();
		if(payment==oldTrip.getBill())
		{
			completed.setCompletedTripsId(randomNum.nextInt(100));
	        completed.setBill(oldTrip.getBill());
	        completed.setTripbookingid(oldTrip.getTripbookingId());
	        completed.setStatus(TripStatus.COMPLETED);
	        completed.setDistanceInKM(oldTrip.getDistanceInKm());
	        completed.setFromDate(oldTrip.getFromDate());
	        completed.setToDate(oldTrip.getToDate());
	        completed.setFromLocation(oldTrip.getFromLocation());
	        completed.setToLocation(oldTrip.getToLocation());
	        completed.setCustomerId(oldTrip.getCustomer().getCustomerId());
	        completed.setDriverId(oldTrip.getDriver().getDriverId());
	        
	        System.out.println("Trip with trip Id = "+tripId+ "is completed");
	        tripBookingDao.deleteById(oldTrip.getTripbookingId());
	        
	        completedTripsDao.save(completed);
		}
		else
		{
			 
			if(payment < oldTrip.getBill())
			{
			  double paisa=oldTrip.getBill()-payment;
			  throw new TripException("Payment should be equal to the Bill="+oldTrip.getBill()+", please add "+ String.format("%.2f", paisa)+ " rupees more");
			}
			
			else if(payment > oldTrip.getBill())
			{
			  double res= payment-oldTrip.getBill() ;
			  throw new TripException("Payment should be equal to the Bill="+oldTrip.getBill()+", please pay "+ String.format("%.2f",res)+ " rupees less");
			}
			
		}
        
        return completed;
	}

}
