package extras;

public class Constants {
	public static final int BANKID = 1;
	
	// Database
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	public static final String DB_URL = "jdbc:mysql://localhost/BANKING_SYSTEM";
	
	//authentication
	public static final String DB_USER = "root";
	public static final String DB_PASS = "1234";	
	
	// Employee types
	public static final String MANAGER = "manager";
	public static final String CLERK = "clerk";
	public static final String TELLER = "teller";
	
	// Bank Table
	public static final class BankTable {	
		public static final String BankTable = "Bank";
		
		public static final String BankID = "BankID";
		public static final String BankName = "BName";
		public static final String BankAddress = "BAddress";
	}
	
	// Customer Table
	public static final class CustomerTable {
		public static final String CustomerTable = "Customer";
		
		public static final String SSN = "SSN";
		public static final String Name = "CName";
		public static final String Phone = "CPhone";
		public static final String Address = "CAddress";
	}
	
	// Employee Table
	public static final class EmpTable {
		public static final String EmpTable = "Employee";
		
		public static final String EmpSSN = "ESSN";
		public static final String EmpType = "EType";
		public static final String EmpSalary = "ESalary";
		public static final String EmpName = "EName";
		public static final String EmpAddress = "EAddress";
		public static final String EmpBirthdate = "EBirthdate";
		public static final String EmpPhone = "EPhone";
		public static final String EmpDepartmentID = "EDepID";
		public static final String EmpSex = "ESex";
		public static final String EmpSince = "ESince";
	}
	
	// Dept_Branch Table
	public static final class DeptBranchTable {
		public static final String DeptBranchTable = "Dept_Branch";
		
		public static final String BranchID = "BrID";
		public static final String DepartmentID = "DeptID";
	}
}
