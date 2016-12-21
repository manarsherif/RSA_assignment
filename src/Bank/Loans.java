package Bank;

import java.sql.Connection;
import java.util.Scanner;

import com.mysql.jdbc.Statement;

public class Loans {
	private Scanner scanner ;
	private Statement stmt;
	private Connection con;
	Loans(Statement stmt,Connection con)
	{
		// TODO Auto-generated constructor stub
		this.scanner=new Scanner(System.in);
		this.stmt=stmt;
		this.con=con;
	}
	/*public int addNewLoan()
	{
		
	}*/

}
