package com.electricitybill.services;

import java.util.List;

import com.electricitybill.entityClass.Customer;

public interface EBillServices {
	//abstract method in java declared
	boolean addCustomer();
	boolean addbemployee();

	void display();

	List<Customer> searchCustomer(String str);

	void generateBill(String cNumber);

	void searchBill(String billNumber);
	
	boolean payTotalBill(String cNumber);
	
	void addSalary();
	
}
