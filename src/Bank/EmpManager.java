package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import extras.Constants;
import extras.Constants.EmpTable;

public class EmpManager {
	
	private static EmpManager empManagerInstance = null;
	
	private Statement stmt;
	private Connection conn;
	
	private EmpManager() {
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
	
	public static EmpManager getEmpManagerInstance() {
		if(empManagerInstance == null)
		{
			empManagerInstance = new EmpManager();
		}
		return empManagerInstance;
	}
	
	public boolean checkEmpLogin(String name, String pass, String type) {
		String query = "SELECT *"
				+ " FROM " + EmpTable.EmpTable
				+ " WHERE " + EmpTable.EmpName + "=" + "'" + name + "'"
				+ " AND " + EmpTable.EmpSSN + "=" + "'" + pass + "'"
				+ " AND " + EmpTable.EmpType + "=" + "'" + type + "'";
		
		try {
			ResultSet res = stmt.executeQuery(query);
			if(res.next())
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
