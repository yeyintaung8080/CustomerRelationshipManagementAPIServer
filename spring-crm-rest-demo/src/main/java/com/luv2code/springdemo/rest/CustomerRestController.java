package com.luv2code.springdemo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

	//autowiring CustomerService
	@Autowired
	private CustomerService customerService;
	
	//add mapping for get customer list
	@GetMapping("/customers")
	public List<Customer> getCustomers(){
		return customerService.getCustomers();
	}
	
	//add mapping for get single customer
	@GetMapping("/customers/{customerId}")
	public Customer getCustomer(@PathVariable int customerId) {
		Customer customer=customerService.getCustomer(customerId);
		
		if(customer==null) {
			throw new CustomerNotFoundException("Customer id not found -"+ customerId);
		}
		
		return customer;//Jackson will response empty message body if the POJO is null.
	}
	
	//add post mapping for customer post
	@PostMapping("/customers")
	public Customer addCustomer(@RequestBody Customer customer) {
		
		//also just in case the pass an id in JSon ... set id to 0
		//this force Hibernate to save a new Item, otherwise this will force Hibernate to updete
		customer.setId(0);//Save A New Item
		
		customerService.saveCustomer(customer);
		return customer;
	}
	
	//add Put Mapping for Updating Existing Customer
	@PutMapping("/customers")
	public Customer updateCustomer(@RequestBody Customer customer) {
		
		customerService.saveCustomer(customer);
		
		return customer;
	}
	
	//add Delete Mapping for Deleting Customer
	@DeleteMapping("/customers/{customerId}")
	public String deleteCustomer(@PathVariable int customerId) {
		
		//Check Before Deleting
		//If The customer is not already exist, throw NotFoundException
		//If the customer found , delete it
		Customer customer=customerService.getCustomer(customerId);
		
		if(customer==null) {
			throw new CustomerNotFoundException();
		}
		
		customerService.deleteCustomer(customerId);
		
		return "Deleted customer id-"+customerId;
	}
}
