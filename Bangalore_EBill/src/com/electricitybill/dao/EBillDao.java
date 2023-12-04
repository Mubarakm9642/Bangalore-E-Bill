package com.electricitybill.dao;

import java.util.List;

import com.electricitybill.entityClass.Bill;
import com.electricitybill.entityClass.Customer;
import com.electricitybill.entityClass.Employee;
public interface EBillDao {
	boolean addCustomerDao(Customer c);

	boolean addEmployeeDao(Employee e);
	
	List<Customer> getAllCustomers();

	int getCustomersCount();

	boolean validateAdminDao(String uName, String password);

	boolean addBillDao(Bill bill);

	int getBillCount();

	boolean getBillByBillNumberDao(String billNumber);

	boolean payBillByBillNumberDao(String cNumber, String billNumber);

	boolean isBillPaid(String billNumber);

	boolean updateUnitsConsumed(String cNumber, int units);

	int getTotalUnits(String cNumber);

	double getTotalBillDao(String cNumber);

	boolean payTotalBill(String cNumber);

	void showBillDetails(String cNumber);

	double getUnitPrice();
	
	void updateUnitPrice(double unitprice);
}
