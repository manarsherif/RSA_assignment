package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import extras.Constants;

public class AccountManager {
	private static AccountManager accountManagerInstance = null;
	private Statement stmt;
	private Connection conn;

	private AccountManager() {
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
	
	public static AccountManager getAccountManagerInstance() {
		if(accountManagerInstance == null)
		{
			accountManagerInstance = new AccountManager();
		}
		return accountManagerInstance;
	}
	
	public int  addNewAccount(float balance,String cssn,String Account_type,int br_id ,float interest_rate ) throws SQLException
	{
		int account_number=(int)(Math.random()*999999);
		String query ="SELECT * FROM Customer WHERE SSN='" + cssn + "'";
		ResultSet rs=stmt.executeQuery(query);
		if(! rs.next())//not a customer then takes his/her data and add him/her to customers 
		{
			return -1;
		}
		String date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
		PreparedStatement pst = (PreparedStatement) conn.prepareStatement(
						"INSERT INTO Account (AccNumber, AccBalance, BrID, CSSN, AType, since)VALUES (?,?,?,?,?,?);");
		
		pst.setString(1, Integer.toString(account_number));
		pst.setString(2, Float.toString(balance));
		pst.setString(3, Integer.toString(br_id));
		pst.setString(4, cssn);
		pst.setString(5, Account_type);
		pst.setString(6, date);
		pst.executeUpdate();
		if(Account_type=="0")
		{
			query = "INSERT INTO SavingsAccount (SavingsAccNum, SInterestRate) VALUES	("
						+Integer.toString(account_number)+", "+interest_rate+");";
		}
		else
		{
			query="INSERT INTO LoanAccount (LoanAccNum) VALUES	("+Integer.toString(account_number)+");";
		}
		stmt.execute(query);
		System.out.println("account is added successfuly");
		return account_number;
	}
	

}
