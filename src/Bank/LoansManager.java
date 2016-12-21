package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import extras.Constants;

public class LoansManager {
	
private static LoansManager loansManagerInstance = null;
	
	private Statement stmt;
	private Connection conn;
	
	private LoansManager() {
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
	
	public static LoansManager getLoansManagerInstance() {
		if(loansManagerInstance == null)
		{
			loansManagerInstance = new LoansManager();
		}
		return loansManagerInstance;
	}
	
	public int addNewLoan(int LoanAccNumber,float loanAmount,float interestRate,String due_date) throws SQLException
	{
		int LoanId=(int)(Math.random()*999999);
		String query= "INSERT INTO Loan (LoanID, LAccNum, LAmount, LInterestRate, DueTime) VALUES	("+
						LoanId+","+Integer.toString(LoanAccNumber)+","+loanAmount+","+interestRate+",'"+due_date+"');";
		stmt.execute(query);
		return LoanId;
		
	}

}
