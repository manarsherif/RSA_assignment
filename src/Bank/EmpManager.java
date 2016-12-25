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

	public ArrayList<ArrayList<String>> Watchsupervisor(String ESSN, String inEname)
			throws SQLException {
		String query = "SELECT Ename,ESSN,Ephone FROM Employee where  ESSN IN"
				+ " (SELECT SupervisorSSN FROM Supervisor,Employee where ESSN = '"
				+ ESSN + "'" + " AND Ename= '" + inEname + "' AND EmpSSN=ESSN);";
		ResultSet rs = stmt.executeQuery(query);
		ArrayList<ArrayList<String>> array = new ArrayList<>();

		while (rs.next())
		{
			ArrayList<String> Supervisor = new ArrayList<>();
			String ssn = rs.getString(EmpTable.EmpSSN);
			String name = rs.getString(EmpTable.EmpName);
			String phone = rs.getString(EmpTable.EmpPhone);
			Supervisor.add(ssn);
			Supervisor.add(name);
			Supervisor.add(phone);

			array.add(Supervisor);
		} 
		return array;
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

	public boolean updateEmployeeSalery(String essn,float esalery) throws SQLException
	{
		String q =  "SELECT EType FROM Employee WHERE ESSN='" + essn + "'" ;
		ResultSet rs = stmt.executeQuery(q);
		if(rs.next())
		{
			String type = rs.getString("EType");
			if (type.equals("Manager"))
			{
				return false;
			}
			else 
			{
				String query="UPDATE "+EmpTable.EmpTable+
						" SET "+EmpTable.EmpSalary+"="+ "'"+esalery+"'"
						+" WHERE "+EmpTable.EmpSSN+"="+ "'"+essn+"'";
				stmt.execute(query);
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	public boolean AddSupervisor(String ESSN, String SupSSN) throws SQLException
	{
		String query1 = "SELECT Etype FROM Employee WHERE  ESSN='" + SupSSN + "'";
		String query2 = "SELECT Etype FROM Employee WHERE  ESSN='" + ESSN + "'";
		ResultSet rs1 = stmt.executeQuery(query1);
		String type1 = "";
		String type2 = "";
		if(ESSN.equals(SupSSN))
		{
			return false;
		}
		if (rs1.next()) 
		{
			type1 = rs1.getString(EmpTable.EmpType);
		}
		else
		{
			return false;
		}
		ResultSet rs2 = stmt.executeQuery(query2);
		if(rs2.next())
		{
			type2 = rs2.getString(EmpTable.EmpType);
			if (type2.equals("Manager") && !type1.equals("Manager"))
			{
				return false;
			}
			else
			{
				String query3= "INSERT INTO Supervisor (SupervisorSSN, EmpSSN) VALUES ('" + SupSSN + "', '" + ESSN + "');";
				stmt.execute(query3); 
				return true;
			}
		}
		else
		{
			return false;
		}
	}
}

