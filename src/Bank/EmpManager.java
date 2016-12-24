package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.mysql.jdbc.Statement;

import extras.Constants;
import extras.Constants.DeptBranchTable;
import extras.Constants.EmpTable;
import extras.Globals;

import java.util.ArrayList;
import java.util.Calendar;

public class EmpManager {

	private static EmpManager empManagerInstance = null;

	private Statement stmt;
	private Connection conn;

	private EmpManager() {
		try {
			Class.forName(Constants.JDBC_DRIVER);
			conn = (Connection) DriverManager.getConnection(Constants.DB_URL,
					Constants.DB_USER, Constants.DB_PASS);
			stmt = (Statement) conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static EmpManager getEmpManagerInstance() {
		if (empManagerInstance == null) {
			empManagerInstance = new EmpManager();
		}
		return empManagerInstance;
	}

	public int checkEmpLogin(String name, String pass, String type) {
		String query = "SELECT " + DeptBranchTable.BranchID + " FROM "
				+ EmpTable.EmpTable + ", " + DeptBranchTable.DeptBranchTable
				+ " WHERE " + EmpTable.EmpName + "=" + "'" + name + "'"
				+ " AND " + EmpTable.EmpSSN + "=" + "'" + pass + "'" + " AND "
				+ EmpTable.EmpType + "=" + "'" + type + "'" + " AND "
				+ EmpTable.EmpDepartmentID + "=" + DeptBranchTable.DepartmentID;

		try {
			ResultSet res = stmt.executeQuery(query);
			if (res.next()) {
				int BranchID = Integer.parseUnsignedInt(res
						.getString(DeptBranchTable.BranchID));
				return BranchID;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public boolean addNewEmployee(String essn, String etype, float esalery,
			String ename, String eaddress, String ebirthdate, String ephone,
			String esex) throws SQLException {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
				.getInstance().getTime());

		String depIDquery = "SELECT EDepID FROM Employee WHERE ESSN='"
				+ Globals.SessionESSN + "' AND EType='Manager'";
		ResultSet rs = stmt.executeQuery(depIDquery);
		if (!rs.next()) {
			return false;
		}
		int deptID = Integer.parseInt(rs.getString(EmpTable.EmpDepartmentID));
		String query = "";
		if (ebirthdate.isEmpty()) {
			query = "INSERT INTO " + EmpTable.EmpTable + " (" + EmpTable.EmpSSN
					+ ", " + EmpTable.EmpType + ", " + EmpTable.EmpSalary
					+ ", " + EmpTable.EmpName + ", " + EmpTable.EmpAddress
					+ ", " + EmpTable.EmpPhone + ",  "
					+ EmpTable.EmpDepartmentID + ",  " + EmpTable.EmpSex
					+ ",  " + EmpTable.EmpSince + ") VALUES ('" + essn + "', '"
					+ etype + "', " + esalery + ", '" + ename + "', '"
					+ eaddress + "', '" + ephone + "', '" + deptID + "', '"
					+ esex + "', '" + date + "');";
		} else {
			query = "INSERT INTO " + EmpTable.EmpTable + " (" + EmpTable.EmpSSN
					+ ", " + EmpTable.EmpType + ", " + EmpTable.EmpSalary
					+ ", " + EmpTable.EmpName + ", " + EmpTable.EmpAddress
					+ ", " + EmpTable.EmpBirthdate + ", " + EmpTable.EmpPhone
					+ ",  " + EmpTable.EmpDepartmentID + ",  "
					+ EmpTable.EmpSex + ",  " + EmpTable.EmpSince
					+ ") VALUES ('" + essn + "', '" + etype + "', " + esalery
					+ ", '" + ename + "', '" + eaddress + "', '" + ebirthdate
					+ "', '" + ephone + "', '" + deptID + "', '" + esex
					+ "', '" + date + "');";
		}
		try {
			stmt.execute(query);
			if (etype.equals(Constants.CLERK)) {
				String subquery = "INSERT INTO Clerk VALUES ('" + essn + "');";
				stmt.execute(subquery);
			} else if (etype.equals(Constants.TELLER)) {
				String subquery = "INSERT INTO Teller VALUES ('" + essn + "');";
				stmt.execute(subquery);
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void Watchsupervisor(int inESSN, String inEname) throws SQLException {
		String query = "SELECT Ename,EDepID FROM Employee where  ESSN IN"
				+ " (SELECT SupervisorSSN FROM Supervisor,Employee where ESSN = '"
				+ inESSN + "'" + " AND Ename= '" + inEname + "');";
		stmt.executeUpdate(query);
	}

	public ArrayList<String> viewFromEmployee(String essn) throws SQLException {
		String query = "SELECT * FROM Employee WHERE ESSN='" + essn + "'";
		ResultSet rs = stmt.executeQuery(query);

		ArrayList<String> array = new ArrayList<String>();
		if (rs.next()) {
			String ssn = rs.getString(EmpTable.EmpSSN);
			String type = rs.getString(EmpTable.EmpType);
			float s = rs.getFloat(EmpTable.EmpSalary);
			String name = rs.getString(EmpTable.EmpName);
			String address = rs.getString(EmpTable.EmpAddress);
			String birthdate = rs.getString(EmpTable.EmpBirthdate);
			String phone = rs.getString(EmpTable.EmpPhone);
			String sex = rs.getString(EmpTable.EmpSex);
			String department = rs.getString(EmpTable.EmpDepartmentID);
			
			String salery = String.valueOf(s);

			array.add(ssn);
			array.add(type);
			array.add(salery);
			array.add(name);
			array.add(address);
			array.add(birthdate);
			array.add(phone);
			array.add(sex);
			array.add(department);

		} else {
			array.clear();
		}
		return array;
	}
	
	public void updateEmployeeSalery(String essn,float esalery) throws SQLException
	{
		String query="UPDATE "+EmpTable.EmpTable+
                     " SET "+EmpTable.EmpSalary+"="+ "'"+esalery+"'"
					+" WHERE "+EmpTable.EmpSSN+"="+ "'"+essn+"'";
                                        
		System.out.println("Salery update is completed");

		try {
			stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
