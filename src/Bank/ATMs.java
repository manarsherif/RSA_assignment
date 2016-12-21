package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;
import com.mysql.jdbc.Statement;

import extras.Constants;

public class ATMs{

	private Statement stmt;
	private Connection conn;
	private List <Integer> removedATM = new ArrayList<Integer>();
        

	ATMs()
	{

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

        public void addNewATM(int cBankID, float ATM_Cash, String ATM_location, boolean inBank) throws SQLException
		{
            String query = "INSERT INTO ATM(CBankID, ATMcash, ATMlocation, InBank) VALUES ("+ cBankID + "," +  ATM_Cash + "," + ATM_location + "," + inBank + ");" ;
			stmt.execute(query);
		}

		public int get_ATM_ID(int cBankID,  String ATM_location) throws SQLException
		{
                        int ATM_ID;
			String query = "SELECT ATMID FROM ATM WHERE cBankID ="+ cBankID + "AND ATM_location = '" + ATM_location + "'" ;
			ResultSet rs = stmt.executeQuery(query);
			ATM_ID = rs.getInt("ATMID");
			return ATM_ID;
		}

		public void removeATM(int ATM_ID) throws SQLException
		{
			String query = "DELETE FROM ATM WHERE ATMID ="+ ATM_ID + ";";
			stmt.execute(query);
			removedATM.add(ATM_ID);
		}
};