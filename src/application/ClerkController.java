package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import Bank.AccountManager;
import Bank.CustomerManager;
import extras.Alerts;
import extras.Dialogs;
import extras.Globals;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ClerkController implements Initializable {
	
	@FXML
	private Label bank_name;
	
	public void AddAccount(ActionEvent event) {
		String[] fields = {"SSN", "Balance", "Interest Rate"};
		String[] fieldsHints = {"Customer SSN", "Account Balance", "Account Interest Rate"};
		Optional<ArrayList<String>> result1 = Dialogs.openDialog(fields, fieldsHints, "Add", "Add Savings Account", fields.length);
		if(result1.isPresent())
		{
			ArrayList<String> accResult = result1.get();
			AccountManager accountManagerInstance = AccountManager.getAccountManagerInstance();
			try {
				int AccNum = accountManagerInstance.addNewAccount(Float.parseFloat(accResult.get(1)), accResult.get(0), 
						"0", Globals.BranchID, Float.parseFloat(accResult.get(2)));
				if(AccNum == -1)
				{
					String[] fields2 = {"Name", "Phone", "Address"};
					String[] fieldsHints2 = {"Customer Name", "Customer Phone", "Customer Address"};
					Optional<ArrayList<String>> result2 = Dialogs.openDialog(fields2, fieldsHints2, "Add", "Add Customer", fields2.length);
					if(result2.isPresent())
					{
						ArrayList<String> cusResult = result2.get();
						CustomerManager customerManagerInstance = CustomerManager.getCustomerManagerInstance();
						customerManagerInstance.addNewCustomer(accResult.get(0), cusResult.get(0), cusResult.get(1), cusResult.get(2));				
						AccNum = accountManagerInstance.addNewAccount(Float.parseFloat(accResult.get(1)), accResult.get(0), 
								"0", Globals.BranchID, Float.parseFloat(accResult.get(2)));
						Alerts.createInfoAlert("Add Savings Account", "Account were added successfully", "Your Account Number is "+AccNum);
					}
				}
				else
				{
					Alerts.createInfoAlert("Add Savings Account", "Account were added successfully", "Your Account Number is "+AccNum);
				}
				} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void AddCustomer(ActionEvent event) {
		String[] fields = {"Name", "SSN", "Phone", "Address"};
		String[] fieldsHints = {"Customer Name", "Customer SSN", "Customer Phone", "Customer Address"};
		Optional<ArrayList<String>> res = Dialogs.openDialog(fields, fieldsHints, "Add", "Add Customer", fields.length);
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			CustomerManager customerManagerInstance = CustomerManager.getCustomerManagerInstance();
			try {
				customerManagerInstance.addNewCustomer(result.get(1), result.get(0), result.get(2), result.get(3));
				Alerts.createInfoAlert("Add Customer", "Customer were added successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void LogOut(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) bank_name.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
        Parent root = loader.load();
        Scene scene = bank_name.getScene();
		scene.setRoot(root);	
        primaryStage.setScene(scene);
        Globals.SessionESSN = "";
        Globals.BranchID = 0;
        primaryStage.show();	
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bank_name.setText(Globals.BankName);
	}
	
}
