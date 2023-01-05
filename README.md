# Cab_Booking_Application
# Online Cab Booking Spring Boot Application

The approach in which riders and drivers are available on a single platform is known as online cab booking. Riders can hire a vehicle in a single click to travel to any place by selecting their preferred vehicle type. The nearest driver will accept the booking. The customer is then picked up and dropped off at their location by the driver.

The goal of an online cab booking system is to bring riders and drivers together on one platform. The online cab booking system makes traveling easier in this digital age by allowing you to move from one location to another with a single tap.


## Tech Stack

- Core Java
- Spring Framework
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- PostMan
- Swagger
- Lombok

## Customer Module

# Customer should  be able to perform below operations:  

- Register Pages, Login 
- Can see all available cabs
- Book trip
- Cancel trip
- Marks journey as completed with payment features
- Update details
- Delete customer details from database
- Logout

## Driver Module

# Driver should be able to perform below operations:

- Register Pages, Login
- Updates status for availability, driver is online or not
- Update driver details
- Delete driver details from database;
- Logout

## Installation & Run

- You need update the database configuration in the application.properties file before launching the API server.
- Change the port number, username, and password to match your local database configuration.

```
    server.port=8889

    spring.datasource.url=jdbc:mysql://localhost:3306/cabbooking_app;
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.username=root
    spring.datasource.password=3511
    
    #ORM s/w specific properties
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true

    spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER


```

## API Root Endpoint

`http://localhost:8889/swagger-ui/`

## API Module Endpoints

### Customer Module

![Screenshot (1717)](https://user-images.githubusercontent.com/101390725/210823727-4a04e19d-e667-403f-8357-c99ce712f198.png) 

### Sample API Response for Customer Login

`POST localhost:8889/customer/login`

- Request Body

```
	{
  		"mobile": "string",
                "password": "string"
	}
```

- Response

```
- Customer current session
   {
	  "sessionId": 2,
	  "userId": 1,
	  "uuid": "8e4f35ff",
	  "userType": "Customer",
	  "sessionStartTime": "2023-01-05T21:08:35.8395149",
	  "sessionEndTime": "2023-01-05T22:08:35.8395149"
  }

```

### Driver Module

![Screenshot (1720)](https://user-images.githubusercontent.com/101390725/210823236-5975a29c-341e-4f38-8a06-45dc829f2aab.png)


## Video Explainer of flow control

<a href="https://drive.google.com/file/d/1Rd4X7QPTFs5r3PWmyLEfo6529FRw_oXs/view?usp=sharing">**Video Drive Link** </a> 


---

 ![6](https://user-images.githubusercontent.com/36689521/204778350-49507557-c070-477b-a571-052fc593ea72.jpg)

---


