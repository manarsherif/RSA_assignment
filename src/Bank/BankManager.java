package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import extras.Constants;
import extras.Constants.BankTable;

public class BankManager {
	private static BankManager bankManagerInstance = null;
	private Statement stmt;
	private Connection conn;
	
	private BankManager() {
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
	
	public static BankManager getBankManagerInstance() {
		if(bankManagerInstance == null)
		{
			bankManagerInstance = new BankManager();
		}
		return bankManagerInstance;
	}
	
	public String getBankName(int bank_id) {
		String query = "SELECT *"
				+ " FROM " + BankTable.BankTable 
				+ " WHERE " + BankTable.BankID + "=" + bank_id;
		String name = null;
		try {
			ResultSet res = stmt.executeQuery(query);
			if(res.next())
			{
				name = String.valueOf(res.getString(Constants.BankTable.BankName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}
	public void Watchsupervisor(int inESSN , String inEname) throws SQLException
	{
		String query ="SELECT Ename,EDepID FROM Employee where  ESSN IN"+" (SELECT SupervisorSSN FROM Supervisor,Employee where ESSN = '"+inESSN+"'"
                    +" AND Ename= '"+ inEname+"');";
		stmt.executeUpdate(query);
	}
}
