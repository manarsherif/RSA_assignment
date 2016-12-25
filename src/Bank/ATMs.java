package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;

import com.mysql.jdbc.Statement;

import extras.Constants;

public class ATMs{

	private static ATMs atmManagerInstance = null;
	private Statement stmt;
	private Connection conn;
	private List <Integer> removedATM = new ArrayList<Integer>();

	public static ATMs getATMsManagerInstance() {
		if(atmManagerInstance == null)
		{
			atmManagerInstance = new ATMs();
		}
		return atmManagerInstance;
	}

	private ATMs()
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
		String query = "INSERT INTO ATM(CBankID, ATMcash, ATMlocation, InBank) VALUES ("+ cBankID + "," +  ATM_Cash + ",'" + ATM_location + "'," + inBank + ");" ;
		stmt.executeUpdate(query);
	}

	public List <Integer> get_ATM_ID(int cBankID,  String ATM_location) throws SQLException
	{
		List <Integer> ATM_IDs = new ArrayList <Integer>();
		String query = "SELECT ATMID FROM ATM WHERE cBankID ="+ cBankID + "AND ATM_location = '" + ATM_location + "'" ;
		ResultSet rs = stmt.executeQuery(query);
		if (!rs.next())
		{
			ATM_IDs.add(-1);
		}   
		while(rs.next())
		{
			ATM_IDs.add(rs.getInt("ATMID"));
		}
		rs.close();

		return ATM_IDs;
	}
        public int count_ATM() throws SQLException
        {
                String query = "SELECT COUNT(*) FROM ATM;";
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                int count = rs.getInt("COUNT(*)");
                return count;
                
        }

	public boolean removeATM(int ATM_ID) throws SQLException
	{
		boolean rs;
		String query = "DELETE FROM ATM WHERE ATMID ="+ ATM_ID + ";";
		if (stmt.executeUpdate(query) > 0)
		{
			removedATM.add(ATM_ID);
			rs  = true;
		}
		else
		{
			rs = false;
		}
		return rs;
	}

	public String[][] show_ATM_Info(int ATM_ID) throws SQLException
	{
                int rows  = count_ATM();
                String[][] ATM_Info = new String[rows][5];
		if (ATM_ID == -1)
		{
			String query = "Select * FROM ATM;" ;
			ResultSet rs = stmt.executeQuery(query);
                        while(rs.next())
                        {
                            for (int row = 0; row < rows; row ++)
                            {
                                ATM_Info[row][0] = String.valueOf(rs.getInt("ATMID"));
                                ATM_Info[row][1] = String.valueOf(rs.getInt("CBankID"));
                                ATM_Info[row][2] = String.valueOf(rs.getInt("ATMcash"));
                                ATM_Info[row][3] = rs.getString("ATMlocation");
                                ATM_Info[row][4] = String.valueOf(rs.getInt("InBank"));
                            }
                           
                        }
                        
                        
		}
		else
		{
			String query_ = "Select * FROM ATM WHERE ATM_ID =" + ATM_ID + ";";
			ResultSet rs = stmt.executeQuery(query_);
                        ATM_Info[0][0] = String.valueOf(rs.getInt("ATMID"));
                        ATM_Info[0][1] = String.valueOf(rs.getInt("CBankID"));
                        ATM_Info[0][2] = String.valueOf(rs.getInt("ATMcash"));
                        ATM_Info[0][3] = rs.getString("ATMlocation");
                        ATM_Info[0][4] = String.valueOf(rs.getInt("InBank"));

		}
                return ATM_Info;
	}

};