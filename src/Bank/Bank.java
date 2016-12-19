package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Bank {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Statement stmt = null;
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				con=(Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system","root","1234");
				stmt=(Statement) con.createStatement();
				//System.out.println("connection succeded");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true)
		{
			System.out.println("Please choose one of the following operations");
			System.out.println("1. Do a transaction");
			System.out.println("2. Add a new account");
			System.out.println("3. Review a customer's account");
			System.out.println("4. get a manager's contact");
			System.out.println("5. exit");
			Scanner scanner = new Scanner(System.in);
			String input= scanner.nextLine();
			switch (input)
			{
			case "1"://transaction
				System.out.println("please enter the transaction Type");
				System.out.println("1. Withdraw");
				System.out.println("2. Deposit");
				String input2=scanner.nextLine();
				switch (input2)
				{
				case "1"://withdraw
					System.out.println("please enter the amount you want to withdraw");
					float amount = Float.parseFloat(scanner.nextLine());
					System.out.println("please enter account number");
					String account_number=scanner.nextLine();
					String query ="SELECT AccBalance FROM Account WHERE AccNumber="+account_number+";";
					ResultSet rs=stmt.executeQuery(query);
					if(rs.next())
					{
						float balance =Float.parseFloat(rs.getString("AccBalance"));
						if(amount>balance)
							System.out.println("You don't have enough money in your accont");
						else
						{
							balance=balance-amount;
							query="UPDATE Account SET AccBalance="+Float.toString(balance)+"WHERE AccNumber="+account_number+";";
							stmt.execute(query);
							System.out.println("Withdrawal succeded");
						}
					}
					else
					{
						System.out.println("You entered a non exisiting account");
						System.out.println("Abort");
					}
					break;
				case "2"://deposit
					System.out.println("please enter the amount you want to Deposit");
					float amount1 = Float.parseFloat(scanner.nextLine());
					System.out.println("please enter account number");
					String account_number1=scanner.nextLine();
					String query1 ="SELECT AccBalance FROM Account WHERE AccNumber="+account_number1+";";
					ResultSet rs1=stmt.executeQuery(query1);
					if(rs1.next())
					{
						float balance =Float.parseFloat(rs1.getString("AccBalance"));
						balance=balance+amount1;
						query1="UPDATE Account SET AccBalance="+Float.toString(balance)+"WHERE AccNumber="+account_number1+";";
						stmt.execute(query1);
						System.out.println("Deposit succeded");	
					}
					else
					{
						System.out.println("You entered a non exisiting account");
						System.out.println("Abort");
					}
					
				}
				break;
			case "2"://review a customer's account
				int account_number=(int)(Math.random()*999999);
				System.out.println("Please enter the starting balance of the account");
				String balance = scanner.nextLine();
				System.out.println("Please enter the type of the acount");
				System.out.println("1. Savings account");
				System.out.println("2. Loans account");
				String acount_type =scanner.nextLine();
				System.out.println("Please enter the customer's social security number");
				String cssn =scanner.nextLine();//must be an existing customer
				String query ="SELECT * FROM Customer WHERE SSN="+cssn;
				ResultSet rs=stmt.executeQuery(query);
				if(! rs.next())//not a customer then takes his/her data and add him/her to customers 
				{
					System.out.println("Please enter the customer's full name");
					String name=scanner.nextLine();
					System.out.println("Please enter the customer's phone number");
					String phone=scanner.nextLine();
					System.out.println("Please enter the customer's address");
					String address=scanner.nextLine();
					query="INSERT INTO Customer (SSN, CNAME, CPhone, CAddress) VALUES ('"+cssn+"','"+ name+"', '"+phone+"', '"+address+"');";
					System.out.println("Welcome to our bank Mr/Mrs"+name);
					stmt.execute(query);
				}
				String date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
				switch(acount_type)
				{
				case "1"://savings account
					//query = "INSERT INTO Account (AccNumber, AccBalance, BrID, CSSN, AType, Since)VALUES ("+Integer.toString(account_number)+","+balance+",2,"+cssn+", 0,"+date+");";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement("INSERT INTO Account (AccNumber, AccBalance, BrID, CSSN, AType, Since)VALUES (?,?,?,?,?,?);");
					pst.setString(1, Integer.toString(account_number));
					pst.setString(2, balance);
					pst.setString(3, "2");
					pst.setString(4, cssn);
					pst.setString(5, "0");
					pst.setString(6, date);
					//System.out.println(query);
					//PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
					//stmt.execute(query);
					pst.executeUpdate();
					System.out.println("account is added successfuly");
					break;
				case "2"://loan account 
					query = "INSERT INTO Account (AccNumber, AccBalance, CSSN, AType)VALUES	("+Integer.toString(account_number)+","+  balance+","+cssn+"1);";
					stmt.execute(query);
					System.out.println("account is added successfuly");
					break;
				}
			case "5":
				break;
			}
			scanner.close();
		}
			
		}

}
