package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import Bank.AccountManager;
import Bank.TransactionsManager;
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

public class TellerController implements Initializable {

	@FXML
	private Label bank_name;
	
	public void Deposit(ActionEvent event) {
		String[] fields = {"Account Number", "Amount"};
		String[] fieldsHints = {"Account Number", "Amount"};
		Optional<ArrayList<String>> res = Dialogs.openFieldsDialog(fields, fieldsHints, "", "Add", "Deposit");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			TransactionsManager transManagerInstance = TransactionsManager.getTransactionsManagerInstance();
			try {
				float balance = transManagerInstance.deposit(Float.parseFloat(result.get(1)), result.get(0));
				if(balance == -1)
				{
					Alerts.createWarningAlert("Deposit", "You entered a non exisiting account");
				}
				else
				{
					Alerts.createInfoAlert("Deposit", "Your current balance is "+balance);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Withdraw(ActionEvent event) {
		String[] fields = {"Account Number", "Amount"};
		String[] fieldsHints = {"Account Number", "Amount"};
		Optional<ArrayList<String>> res = Dialogs.openFieldsDialog(fields, fieldsHints, "", "Add", "Withdraw");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			TransactionsManager transManagerInstance = TransactionsManager.getTransactionsManagerInstance();
			try {
				float balance = transManagerInstance.withdraw(Float.parseFloat(result.get(1)), result.get(0));
				if(balance == -1)
				{
					Alerts.createWarningAlert("Withdraw", "You entered a non exisiting account");
				}
				else if(balance == -2)
				{
					Alerts.createWarningAlert("Withdraw", "You don't have enough money in your account");
				}
				else
				{
					Alerts.createInfoAlert("Withdraw", "Your current balance is "+balance);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void ViewAccount(ActionEvent event) throws IOException { 
		String[] fields = {"Account Number"};
		String[] fieldsHints = {"Account Number"};
		Optional<ArrayList<String>> res = Dialogs.openFieldsDialog(fields, fieldsHints, "", "View", "View Account Details");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			AccountManager accountManagerInstance = AccountManager.getAccountManagerInstance();
			try {
				ArrayList<String> details = accountManagerInstance.viewAccount(Integer.parseInt(result.get(0)));
				if(details.isEmpty())
				{
					Alerts.createWarningAlert("View Account Details", "You entered a non exisiting account");
				}
				else
				{
					String print = "";
					for (String detail : details) {
						print += detail + "\n";
					}
					Alerts.createInfoAlert("View Account Details", "Account Details for "+result.get(0), print);
				}
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
