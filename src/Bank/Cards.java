package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import extras.Constants;

public class Cards{

	private static Cards cardManagerInstance = null;
	
	private Statement stmt;
	private Connection conn;

	public static Cards getCardManagerInstance() {
		if(cardManagerInstance == null)
		{
			cardManagerInstance = new Cards();
		}
		return cardManagerInstance;
	}

	private Cards()
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

	public int addNewCard(int accountNumber, int cardType, float credit) throws SQLException
	{
		int cardSerialNumber=(int)(Math.random()*899999);
		String query = "INSERT INTO Card(SerialNum, AccNum, CType) VALUES ("+ cardSerialNumber + "," +  accountNumber + "," + cardType +");";
		stmt.execute(query);
		if (cardType == 0)
		{
			String Dquery = "INSERT INTO Debit_Card(DSerialNum, Credit) VALUES ("+ cardSerialNumber + "," + credit + ");";
			stmt.execute(Dquery);
		}
		else if(cardType == 1)
		{
			String Cquery = "INSERT INTO Credit_Card(CSerialNum, Payment, SpentCredit) VALUES ("+ cardSerialNumber + "," +  credit + "," + 0 +");";
			stmt.execute(Cquery);
		}
		return cardSerialNumber;
	}

	public void removeCard(int cardSerialNumber) throws SQLException
	{
		String query = "DELETE FROM Card WHERE SerialNum =" + cardSerialNumber + ";";
		stmt.execute(query);
	}
}