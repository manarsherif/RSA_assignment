package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import extras.Constants;
import extras.Constants.DeptBranchTable;
import extras.Constants.EmpTable;

import java.util.Date;

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
	
	public int checkEmpLogin(String name, String pass, String type) {
		String query = "SELECT " + DeptBranchTable.BranchID
				+ " FROM " + EmpTable.EmpTable + ", " + DeptBranchTable.DeptBranchTable
				+ " WHERE " + EmpTable.EmpName + "=" + "'" + name + "'"
				+ " AND " + EmpTable.EmpSSN + "=" + "'" + pass + "'"
				+ " AND " + EmpTable.EmpType + "=" + "'" + type + "'"
				+ " AND " + EmpTable.EmpDepartmentID + "=" + DeptBranchTable.DepartmentID;
		
		try {
			ResultSet res = stmt.executeQuery(query);
			if(res.next())
			{
				int BranchID = Integer.parseUnsignedInt(res.getString(DeptBranchTable.BranchID));
				return BranchID;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	public void addNewEmployee(String essn,String etype, float esalery, String ename, String eadress, String ebirthdate, int ephone, int eDepID, String esex, Date esince) 
	{
	String query="INSERT INTO "+EmpTable.EmpTable+" ("
					+EmpTable.EmpSSN+", "+EmpTable.EmpType+", "
					+EmpTable.EmpSalary+", "+EmpTable.EmpName
					+" , "+EmpTable.EmpAddress+" , "+EmpTable.EmpBirthdate+" , "+EmpTable.EmpPhone
					+" ,  "+EmpTable.EmpDepartmentID+" ,  "+EmpTable.EmpSex+" ,  "+EmpTable.EmpSince
					+" ) VALUES ('"+essn+"','"+ etype+"', '"+esalery+"', '"+ename+"' , '"+eadress+"' , '"+ebirthdate+"' , '"+ephone+"' , '"+eDepID+"' '"+esex+"' , '"+esince+"');";
		System.out.println("Welcome to our bank Mr/Mrs"+ename);
		try {
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
}
	
}
