package Bank;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class AccountManager {
	private Scanner scanner ;
	private Statement stmt;
	private Connection con;
	public AccountManager(Statement stmt,Connection con)
	{
		// TODO Auto-generated constructor stub
		this.scanner=new Scanner(System.in);
		this.stmt=stmt;
		this.con=con;
	}
	public void addNewAccount(String balance,String cssn,String Account_type,String br_id) throws SQLException
	{
		int account_number=(int)(Math.random()*999999);
		String query ="SELECT * FROM Customer WHERE SSN="+cssn;
		ResultSet rs=stmt.executeQuery(query);
		if(! rs.next())//not a customer then takes his/her data and add him/her to customers 
		{
			CustomerManager cm= new CustomerManager(stmt, con);
			cm.addNewCustomer(cssn, name, phone, address);
		}
		String date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
		PreparedStatement pst = (PreparedStatement) con.prepareStatement("INSERT INTO Account (AccNumber, AccBalance, BrID, CSSN, AType, Since)VALUES (?,?,?,?,?,?);");
		pst.setString(1, Integer.toString(account_number));
		pst.setString(2, balance);
		pst.setString(3, br_id);
		pst.setString(4, cssn);
		pst.setString(5, Account_type);
		pst.setString(6, date);
		pst.executeUpdate();
		System.out.println("account is added successfuly");
	}
	

}
