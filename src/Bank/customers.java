package Bank;

import java.sql.*;


public class customers
{
	//creating connection
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/BANKING_SYSTEM";

	//authentication
	static final String USER = "root";
	static final String PASS = "1234";

	public static void main(String[] args)
	{
		Connection conn = null;
		Statement stmt = null;
		Savepoint savepoint1 = null;
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			//set a Savepoint
			savepoint1 = conn.setSavepoint("Savepoint1");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		try
		{
			Class.forName(JDBC_DRIVER);
			System.out.println("Creating Customer Table.");
			String sql = "CREATE TABLE Customer" + 
					"(SSN     CHAR(14)    NOT NULL, " +
					"CName     VARCHAR(50)   NOT NULL, " +
					"CPhone    VARCHAR(20)   NOT NULL, " +
					"CAddress  VARCHAR(255)  NOT NULL, " +
					"PRIMARY KEY (SSN))"; 
			stmt.executeUpdate(sql);
			System.out.println("Customer Table is created.");
			conn.commit();
		}
		catch(SQLException se)
		{
			//Handle errors for JDBC
			try {
				conn.rollback(savepoint1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			se.printStackTrace();
		}
		catch(Exception e)
		{
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		finally
		{
			//finally block used to close resources
			try {
				if(stmt!=null)
					conn.close();
			}
			catch(SQLException se){}// do nothing
			try {
				if(conn!=null)
					conn.close();
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
		}

		//set a Savepoint
		Savepoint savepoint2 = null;
		try {
			savepoint2 = conn.setSavepoint("Savepoint2");
		} catch (SQLException e1) {
			e1.printStackTrace();
		} 
		
		try
		{ 
			
			// the mysql insert statement
			System.out.println("Inserting records into the customer table...");
			String sql = "INSERT INTO CUSTOMER " +
					"VALUES ('29411290103419', 'Haytham Tareq Mohammed Gabr Metawie', '01285190526', '3 Mohamed Attia Street, Ain Shams')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO CUSTOMER "  +
					"VALUES ('26001010101213', 'Ahmed Mohamed Ahmed Ali', '01002266622', '12 Saqr Qurish, Masaken Sheraton')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO CUSTOMER "  +
					"VALUES ('24910080103345', 'Abdelsamad Ibrahim Mohamed Kamel Fekry', '01104568937', '1 AL-Sarayat Street, Al Abbasya')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO CUSTOMER "  +
					"VALUES ('27006140101456', 'Laila Mohamed Ablkhaleq Fahmi', '01228745035', '28 Nazeh Khalefa Street, Heliopolis')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO CUSTOMER "  +
					"VALUES ('25609300102341', 'Soliman Abdelmoaty Gamel Ismail', '01120345930', '21 Mohamed Ismail Street, El Marg')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO CUSTOMER "  +
					"VALUES ('29005170101715', 'Nada Mohamed Talaat Roshdy', '01002347593', '6 Ahmed Saed Street, Al Abbasya')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO CUSTOMER "  +
					"VALUES ('24503160103429', 'Sohir Mohamed Khalil Gomaa', '01228345678', '39 Fathy Mousa Street, Hadaeq El Qoba')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO CUSTOMER "  +
					"VALUES ('28803270104855', 'Kareem Sameh Shokry George', '01220333500', 'El Rahabya Centre, Taha Hussien')";
			stmt.executeUpdate(sql);
			System.out.println("Insertion into the table has done successfully..."); 

			System.out.println("Displaying Table Content...");
			sql = "SELECT * FROM Customer";
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()){
				//Retrieve by column name
				String customer_ssn  = rs.getString("SSN");
				String customer_name = rs.getString("CNAME");
				String phone = rs.getString("CPhone");
				String address = rs.getString("CAddress");

				//Display values
				System.out.print(", Name: " + customer_name);
				System.out.print("SSN: " + customer_ssn);
				System.out.print(", Phone: " + phone);
				System.out.println(", Address: " + address);
			}
			rs.close();
		}
		catch(SQLException se)
		{
			//Handle errors for JDBC
			try {
				conn.rollback(savepoint2);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			se.printStackTrace();
		}
		catch(Exception e)
		{
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		finally
		{
			//finally block used to close resources
			try {
				if(stmt!=null)
					conn.close();
			}
			catch(SQLException se){}// do nothing
			try {
				if(conn!=null)
					conn.close();
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
		}
		System.out.println("Finished!");
	}
}