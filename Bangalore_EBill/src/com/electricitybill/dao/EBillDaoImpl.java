package com.electricitybill.dao;

import com.electricitybill.dbutil.DBUtil;
import com.electricitybill.entityClass.Bill;
import com.electricitybill.entityClass.Customer;
import com.electricitybill.entityClass.Employee;
import com.electricitybill.services.EBillServicesImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class EBillDaoImpl implements EBillDao {
	private DBUtil dbutil = DBUtil.obj;
	private Connection con;
	private Statement st;
	private PreparedStatement pst;
	private ResultSet rs;

	@Override
	public boolean addCustomerDao(Customer c) {
		String addUserQuery = "INSERT INTO CUSTOMERS VALUES (?,?,?,?,?)";
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(addUserQuery);
			pst.setString(1, c.getCNumber());
			pst.setString(2, c.getCName());
			pst.setString(3, c.getCId());
			pst.setString(4, c.getMobile());
			pst.setString(5, c.getCAddress());
			int i = pst.executeUpdate();
			if (i == 1) {
				System.out.println("User Added Sucesfully with id : " + c.getCNumber() + " , Name : " + c.getCName());
				String cNumber = c.getCNumber();
				initUserBill(cNumber);
				return true;
			}
		} catch (Exception e) {
			e.getMessage();
		}

		System.out.println("Duplicate User not added with Aadhar Number : " + c.getCId());
		return false;
	}
	
	@Override
	public boolean addEmployeeDao(Employee e) {
		String addEmployeeQuery = "INSERT INTO Employee (cnumber,cname,cid,mobile,address)VALUES (?,?,?,?,?)";
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(addEmployeeQuery);
			pst.setString(1, e.getCNumber());
			pst.setString(2, e.getCName());
			pst.setString(3, e.getCId());
			pst.setString(4, e.getMobile());
			pst.setString(5, e.getCAddress());
			int i = pst.executeUpdate();
			if (i == 1) {
				System.out.println("Employee Added Sucesfully with id : " + e.getCNumber() + " , Name : " + e.getCName());
				String cNumber = e.getCNumber();
				salaryUpdate(cNumber);
				return true;
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}

		System.out.println("Duplicate Employee not added with Aadhar Number : " + e.getCId());
		return false;
	}

	
	private void salaryUpdate(String cNumber) {
		String initUserQuery = "INSERT INTO esalary (CNUMBER) VALUES ( ? )";
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(initUserQuery);
			pst.setString(1, cNumber);
			int i = pst.executeUpdate();
			if (i > 0) {
				System.out.println("Kindly Add salary for the BESCOM employee........");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initUserBill(String cNumber) {
		String initUserQuery = "INSERT INTO TOTALUNITS (CNUMBER) VALUES ( ? )";
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(initUserQuery);
			pst.setString(1, cNumber);
			int i = pst.executeUpdate();
			if (i > 0) {
				System.out.println("Init Succesfull........");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getCustomersCount() {
		String countQuery = "SELECT COUNT(CNUMBER) FROM CUSTOMERS";
		int count = 0;
		try {
			con = dbutil.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(countQuery);
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<Customer> getAllCustomers() {
		String viewQuery = "SELECT * FROM CUSTOMERS";
		List<Customer> list = new ArrayList<>();
		try {
			con = dbutil.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(viewQuery);
			while (rs.next()) {
				list.add(Customer.builder().cNumber(rs.getString(1)).cName(rs.getString(2)).cId(rs.getString(3))
						.mobile(rs.getString(4)).cAddress(rs.getString(5)).build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int getBillCount() {
		String billCountQuery = "SELECT COUNT(BILLNUMBER) FROM BILL";
		int billCount = 0;
		try {
			con = dbutil.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(billCountQuery);
			while (rs.next()) {
				billCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return billCount;
	}

	@Override
	public int getTotalUnits(String Cnumber) {
		String unitsConsumedQuery = "SELECT TOTALUNITS FROM TOTALUNITS WHERE CNUMBER = ?";
		int units = 0;
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(unitsConsumedQuery);
			pst.setString(1, Cnumber);
			rs = pst.executeQuery();
			while (rs.next()) {
				units = rs.getInt("totalunits");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return units;
	}

	@Override
	public boolean updateUnitsConsumed(String Cnumber, int units) {
		String updateUnitsQuery = "UPDATE TOTALUNITS SET TOTALUNITS = ? WHERE CNUMBER = ? ";
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(updateUnitsQuery);
			pst.setInt(1, units);
			pst.setString(2, Cnumber);
			int i = pst.executeUpdate();
			if (i > 0) {
				System.out.println("Total units updated....");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addBillDao(Bill b) {
		String addBillQuery = "INSERT INTO BILL (CNUMBER,BILLNUMBER,UNITS,BILLAMOUNT) VALUES (?,?,?,?)";
		try {
			con = dbutil.getConnection();
			pst = con.prepareCall(addBillQuery);
			pst.setString(1, b.getCNumber());
			pst.setString(2, b.getBNumber());
			pst.setInt(3, b.getUnits());
			pst.setDouble(4, b.getBillAmount());
			int i = pst.executeUpdate();
			if (i == 1) {
				System.out.println("Bill added successfully ....with bill number : " + b.getBNumber()
						+ " for Customer : " + b.getCNumber());
				setDuedate(b.getCNumber(), b.getBNumber());
				getBillByBillNumberDao(b.getBNumber());
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void setDuedate(String cNumber, String bNumber) {
		String setDueDateQuery = "UPDATE bill SET billduedate = (SELECT DATE_ADD(CURRENT_TIMESTAMP , INTERVAL 30 DAY)) WHERE cnumber=? AND billnumber=?";
		try {
			con = dbutil.getConnection();
			pst = con.prepareCall(setDueDateQuery);
			pst.setString(1, cNumber);
			pst.setString(2, bNumber);
			int i = pst.executeUpdate();
			if (i == 1) {
				System.out.println("Due Date Set Succesfull");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean validateAdminDao(String uName, String password) {
		String checkQuery = "SELECT USERNAME FROM ADMIN WHERE USERNAME = ? and PASSWORD = ?";
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(checkQuery);
			pst.setString(1, uName);
			pst.setString(2, password);
			rs = pst.executeQuery();
			if (rs.next()) {
				System.out.println("Welcome Admin....:-" + rs.getString("username"));
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Invalid login  ");
		return false;
	}

	@Override
	public boolean getBillByBillNumberDao(String billNumber) {
		String getBillQuery = "SELECT * FROM BILL WHERE BILLNUMBER = ?";
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(getBillQuery);
			pst.setString(1, billNumber);
			rs = pst.executeQuery();
			if (rs.next()) {
				paintBillDetails();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("No Bill Found with given Bill Number : " + billNumber);
		return false;

	}

	private void paintBillDetails() throws SQLException {
		String cNumber = rs.getString("cNumber").toUpperCase();
		String bNumber = rs.getString("billNumber");
		int units = rs.getInt("units");
		String billDate = rs.getString("billDate").substring(0, 10);
		String dueDate = rs.getString("billdueDate").substring(0, 10);
		String billPaidDate = rs.getString("billpaiddate");
		double billAmount = rs.getInt("billamount");
		String status = rs.getString("status");
		System.out.println("Bill Details for Customer " + cNumber);
		System.out.println("---------------------------------------");
		System.out.println("Customer Number    : " + cNumber + "\nBillNumber   : " + bNumber + "\nNo of Units : "
				+ units + "\nBill date  : " + billDate + "\nDue date   : " + dueDate + "\nBillAmount : " + billAmount
				+ "\nStatus    : " + status);
		if (status.equals("PAID")) {
			System.out.println("Bill Paid Date :-" + billPaidDate.substring(0, 10));
		}
		System.out.println("----------------------------------------");
	}

	@Override
	public boolean payBillByBillNumberDao(String cNumber, String billNumber) {
		String payBillQuery = "UPDATE BILL SET STATUS = ? WHERE CNUMBER = ? AND BILLNUMBER = ?";
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(payBillQuery);
			pst.setString(1, "PAID");
			pst.setString(2, cNumber);
			pst.setString(3, billNumber);
			int i = pst.executeUpdate();
			if (i == 1) {
				System.out.println("Bill Payed Successfull for Bill Number : " + billNumber);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isBillPaid(String billNumber) {
		String isPaidQuery = "SELECT STATUS FROM BILL WHERE STATUS = ? AND BILLNUMBER =? ";
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(isPaidQuery);
			pst.setString(1, "PAID");
			pst.setString(2, billNumber);
			rs = pst.executeQuery();
			while (rs.next()) {
				System.out.println("Bill Already Paid for Bill Number : " + billNumber.toUpperCase());
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Bill Not Paid Bill Number : " + billNumber.toUpperCase()+ " You may continue to Pay");
		return false;
	}

	@Override
	public double getTotalBillDao(String cNumber) {
		String totalBillQuery = "SELECT SUM(BILLAMOUNT) FROM BILL WHERE STATUS=? AND CNUMBER=?";
		double totalDueBill = 0;
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(totalBillQuery);
			pst.setString(1, "NOTPAID");
			pst.setString(2, cNumber);
			rs = pst.executeQuery();
			while (rs.next()) {
				totalDueBill = rs.getDouble(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalDueBill;
	}

	@Override
	public boolean payTotalBill(String cNumber) {
		String updateBillPaidQuery = "UPDATE BILL SET STATUS =? WHERE CNUMBER =? AND STATUS=?";
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(updateBillPaidQuery);
			pst.setString(1, "PAID");
			pst.setString(2, cNumber);
			pst.setString(3, "NOTPAID");
			int i = pst.executeUpdate();
			if (i > 0) {
				System.out.println("Bill Paid Sucessfully for Customer with C-Number " + cNumber);
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Error Occured while paying bill for Cnumber : " + cNumber);
		return false;
	}

	@Override
	public void showBillDetails(String cNumber) {
		String showBillQuery = "SELECT * FROM BILL WHERE CNUMBER =?";
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(showBillQuery);
			pst.setString(1, cNumber);
			rs = pst.executeQuery();
			while (rs.next()) {
				paintBillDetails();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public double getUnitPrice() {
		String unitPriceQuery = "SELECT UNITCOST FROM UNITSCOST";
		try {
			con = dbutil.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(unitPriceQuery);
			if (rs.next()) {
				return rs.getDouble(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 4.45; // Base unit cost price
	}

	@Override
	public void updateUnitPrice(double unitprice) {
		String updateQuery = "UPDATE UNITSCOST SET UNITCOST = ?";
		double oldUnitRate = getUnitPrice();
		try {
			con = dbutil.getConnection();
			pst = con.prepareStatement(updateQuery);
			pst.setDouble(1, unitprice);
			int i = pst.executeUpdate();
			if (i == 1) {
				System.out.println("UnitRate Update Succesfull... from " + oldUnitRate + " to " + unitprice);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
