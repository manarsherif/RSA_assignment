package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import com.mysql.jdbc.Statement;
import extras.Constants;

public class Cards{

	private Statement stmt;
	private Connection conn;
     


	Cards()
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

	public int addNewCard(int accountNumber, boolean cardType, float credit, float payment, float spentCredit) throws SQLException
	{
            int cardSerialNumber=(int)(Math.random()*899999);
            String query = "INSERT INTO Card(SerialNum, AccNum, CType) VALUES ("+ cardSerialNumber + "," +  accountNumber + "," + cardType +");";
            stmt.execute(query);
            if (cardType)
            {
                String Dquery = "INSERT INTO Debit_Card(DSerialNum, Credit) VALUES ("+ cardSerialNumber + "," + credit + ");";
                stmt.execute(Dquery);
            }
            else
            {
                String Cquery = "INSERT INTO Credit_Card(CSerialNum, Payment, SpentCredit) VALUES ("+ cardSerialNumber + "," +  payment + spentCredit +");";
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