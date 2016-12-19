package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mysql.jdbc.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import extras.Constants;;

public class MainController implements Initializable {
	
	@FXML
	private Label bank_name;
	
	@FXML
	private TextField user;
	
	@FXML
	private TextField pass;
	
	@FXML
	private ComboBox<String> type;
	
	private ObservableList<String> EmpTypes = FXCollections.observableArrayList(
			Constants.TELLER,
			Constants.CLERK,
			Constants.MANAGER
            );
	
	
	public void Login(ActionEvent event) {
		
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		type.setItems(EmpTypes);
		
		Statement stmt = null;
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				con=(Connection)DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASS);
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
		String query ="SELECT * FROM Bank WHERE BankID="+Constants.BANKID;
		
		try {
			ResultSet res = stmt.executeQuery(query);
			if(res.next())
			{
				String name = String.valueOf(res.getString(Constants.BankTable.BankName));
				bank_name.setText(name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
