package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import Bank.AccountManager;
import Bank.Cards;
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
		Optional<ArrayList<String>> result1 = Dialogs.openFieldsDialog(fields, fieldsHints, "", "Add", "Add Savings Account");
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
					Optional<ArrayList<String>> result2 = Dialogs.openFieldsDialog(fields2, fieldsHints2, "", "Add", "Add Customer");
					if(result2.isPresent())
					{
						ArrayList<String> cusResult = result2.get();
						CustomerManager customerManagerInstance = CustomerManager.getCustomerManagerInstance();
						customerManagerInstance.addNewCustomer(accResult.get(0), cusResult.get(0), cusResult.get(1), cusResult.get(2));				
						AccNum = accountManagerInstance.addNewAccount(Float.parseFloat(accResult.get(1)), accResult.get(0), 
								"0", Globals.BranchID, Float.parseFloat(accResult.get(2)));
						Alerts.createInfoAlert("Add Savings Account", "Account was added successfully", "Your Account Number is "+AccNum);
					}
				}
				else
				{
					Alerts.createInfoAlert("Add Savings Account", "Account was added successfully", "Your Account Number is "+AccNum);
				}
				} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void AddCustomer(ActionEvent event) {
		String[] fields = {"Name", "SSN", "Phone", "Address"};
		String[] fieldsHints = {"Customer Name", "Customer SSN", "Customer Phone", "Customer Address"};
		Optional<ArrayList<String>> res = Dialogs.openFieldsDialog(fields, fieldsHints, "", "Add", "Add Customer");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			CustomerManager customerManagerInstance = CustomerManager.getCustomerManagerInstance();
			try {
				customerManagerInstance.addNewCustomer(result.get(1), result.get(0), result.get(2), result.get(3));
				Alerts.createInfoAlert("Add Customer", "Customer was added successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void UpdateInfo(ActionEvent event) {
		String[] fields = {"Name", "SSN"};
		String[] fieldsHints = {"Customer Name", "Customer SSN"};
		String[] choices = {"Phone", "Address"};
		Optional<ArrayList<String>> res = Dialogs.openUpdateDialog(fields, fieldsHints, choices, "Update", "Update Customer Information");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			CustomerManager customerManagerInstance = CustomerManager.getCustomerManagerInstance();
			try {
				int success = 0;
				if(result.get(3).equals("Phone")) 
				{
					success = customerManagerInstance.UpdateCustomerPhone(result.get(1), 
							result.get(0), result.get(2));
				}
				else if(result.get(3).equals("Address"))
				{
					success = customerManagerInstance.UpdateCustomerAddress(result.get(1), 
							result.get(0), result.get(2));
				}
				
				if(success > 0)
				{
					Alerts.createInfoAlert("Update Customer Information", 
							"Customer "+result.get(3)+" was updated successfully");
				}
				else
				{
					Alerts.createWarningAlert("Update Customer Information Failed", 
							"Wrong Name or SSN");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void AddCard(ActionEvent event) {
		String[] fields = {"Account Number", "Amount"};
		String[] fieldsHints = {"Account Number", "Credit or Payment"};
		String[] selection = {"Card Type", "Debit Card", "Credit Card"};
		String[] choices = {};
		Optional<ArrayList<String>> res = Dialogs.openGeneralDialog(fields, fieldsHints, choices, "", "", selection, "Add", "Add Card");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			Cards cardManagerInstance = Cards.getCardManagerInstance();
			int type = 0;
			if(result.get(2).equals("Credit Card"))
			{
				type = 1;
			}
			try {
				int cardID = cardManagerInstance.addNewCard(Integer.parseInt(result.get(0)), type, Float.parseFloat(result.get(1)));
				Alerts.createInfoAlert("Add Card", "Card was added successfully", "Card Id is "+ cardID);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void ShowCard(ActionEvent event) {
		String[] fields = {"ID"};
		String[] fieldsHints = {"Card ID"};
		Optional<ArrayList<String>> res = Dialogs.openFieldsDialog(fields, fieldsHints, "", "Show", "Card Info");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			Cards cardManagerInstance = Cards.getCardManagerInstance();
			try {
				ArrayList<String> info = cardManagerInstance.show_card_Info(Integer.parseInt(result.get(0)));
				if(info.isEmpty())
				{
					Alerts.createWarningAlert("Card Info", "Wrong Card Serial Number");	
				}
				else
				{
					int type = Integer.parseInt(info.get(0));
					if(type == 0)
					{
						Alerts.createInfoAlert("Card Info", "Card Serial Number: "+result.get(0),
								"Card Type:\t\tDebit Card\n"+
								"Credit:\t\t" + info.get(1)); 
					}
					else if(type == 1)
					{
						Alerts.createInfoAlert("Card Info", "Card Serial Number: "+result.get(0),
								"Card Type:\t\tCredit Card\n"+
								"Payment:\t\t" + info.get(1)+
								"Spent Credit:\t\t" + info.get(2));
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}

	public void RemoveCard(ActionEvent event) {
		String[] fields = {"ID"};
		String[] fieldsHints = {"Card ID"};
		Optional<ArrayList<String>> res = Dialogs.openFieldsDialog(fields, fieldsHints, "", "Remove", "Remove Card");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			Cards cardManagerInstance = Cards.getCardManagerInstance();
			try {
				cardManagerInstance.removeCard(Integer.parseInt(result.get(0)));
				Alerts.createInfoAlert("Remove Card", "Card was removed successfully");
			} catch (NumberFormatException e) {
				e.printStackTrace();
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
