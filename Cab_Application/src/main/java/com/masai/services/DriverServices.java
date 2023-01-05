package com.masai.services;

import com.masai.modelEntity.Driver;

public interface DriverServices {

	public Driver newDriver(Driver driver);

	public String removeDriver(String name, String key);

	public Driver updateDriver(Driver driver, String key);

	public String updateStatus(String newStatus, String key);

	public String logoutDriver(String key);
}
