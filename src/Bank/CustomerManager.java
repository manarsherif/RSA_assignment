package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import extras.Constants;

public class CustomerManager {
	private static CustomerManager customerManagerInstance = null;
	private Statement stmt;
	private Connection conn;

	private CustomerManager() {
		try {
			Class.forName(Constants.JDBC_DRIVER);
			conn = (Connection) DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASS);
			stmt = (Statement) 	conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static CustomerManager getCustomerManagerInstance() {
		if(customerManagerInstance == null)
		{
			customerManagerInstance = new CustomerManager();
		}
		return customerManagerInstance;
	}
	
	public void addNewCustomer(String cssn,String name,String phone,String address) throws SQLException
	{
		String query="INSERT INTO Customer (SSN, CName, CPhone, CAddress) VALUES "
				+ "('"+cssn+"','"+ name+"', '"+phone+"', '"+address+"');";
		System.out.println("Welcome to our bank Mr/Mrs"+name);
		stmt.execute(query);
	}
	public int UpdateCustomerPhone(String cssn,String name,String phone) throws SQLException
	{		
		String query="UPDATE Customer SET CPhone='"+phone+"'"
					+" WHERE SSN='"+cssn+"'"
                    +" AND CName='"+ name+"'";
		System.out.println("Info update is completed Mr/Mrs"+name);
		return stmt.executeUpdate(query);
	}
	public int UpdateCustomerAddress(String cssn,String name,String address) throws SQLException
	{
		String query="UPDATE Customer SET CAddress='"+address+"'"
				+" WHERE SSN='"+cssn+"'"
                +" AND CName='"+ name+"'";
		System.out.println("Info update is completed Mr/Mrs"+name);
		return stmt.executeUpdate(query);
	}

}
