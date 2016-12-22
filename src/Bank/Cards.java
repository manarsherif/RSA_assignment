package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import extras.Constants;
import java.sql.ResultSet;

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
                boolean rs;
                int cardSerialNumber=(int)(Math.random()*899999);
		String query = "INSERT INTO Card(SerialNum, AccNum, CType) VALUES ("+ cardSerialNumber + "," +  accountNumber + "," + cardType +");";
		if (stmt.executeUpdate(query) > 0)
                    rs = true;
                else 
                    rs = false;
                
		if (cardType == 0)
		{
			String Dquery = "INSERT INTO Debit_Card(DSerialNum, Credit) VALUES ("+ cardSerialNumber + "," + credit + ");";
			stmt.executeUpdate(Dquery);
		}
		else if(cardType == 1)
		{
			String Cquery = "INSERT INTO Credit_Card(CSerialNum, Payment, SpentCredit) VALUES ("+ cardSerialNumber + "," +  credit + "," + 0 +");";
			stmt.executeUpdate(Cquery);
		}
                if (rs)
                    return cardSerialNumber;
                else 
                    return -1;
	}

	public boolean removeCard(int cardSerialNumber) throws SQLException
	{
		boolean rs;
                String query = "DELETE FROM Card WHERE SerialNum =" + cardSerialNumber + ";";
		if (stmt.executeUpdate(query) > 0)
                    rs = true;
                else 
                    rs = false;
                return rs;
	}
        
        public void show_card_Info(int cardSerialNumber) throws SQLException
        {
                String query = "SELECT CType FROM ATM WHERE SerialNum =" + cardSerialNumber + ";";
                ResultSet rs  = stmt.executeQuery(query);
                int type = rs.getInt("CType");
                if (type == 0)
                {
                    String Dquery = "SELECT * FROM Debit_Card WHERE DSerialNum =" + cardSerialNumber + ";";
                    ResultSet D_rs = stmt.ecexcuteQuery(Dquery);
                    while (D_rs.next())
                    {
                        float credit = D_rs.getFloat("Credit");
                        System.out.print("Card Serial Number: " + cardSerialNumber);
                        System.out.print("Credit: " + credit);
                    }
                }
                else if (type == 1)
                {
                    String Cquery = "SELECT * FROM Credit_Card WHERE DSerialNum =" + cardSerialNumber + ";";
                    ResultSet C_rs = stmt.ecexcuteQuery(Cquery);
                    float payment = C_rs.getFloat("Payment");
                    float spentCredit = C_rs.getFloat("SpentCredit");
                        System.out.print("Card Serial Number: " + cardSerialNumber);
                        System.out.print("Payment: " + payment);
                        System.out.print("Spent Credit: " + spentCredit);
                }
        }
}