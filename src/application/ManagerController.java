package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import Bank.AccountManager;
import Bank.CustomerManager;
import Bank.EmpManager;
import Bank.LoansManager;
import extras.Alerts;
import extras.Constants;
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

public class ManagerController implements Initializable {

	@FXML
	private Label bank_name;
	
	public void AddEmployee(ActionEvent event) {
		String[] fields = {"Name", "SSN", "Phone", "Address", "Salary"};
		String[] fieldsHints = {"Employee Name", "Employee SSN", "Employee Phone", "Employee Address", "Employee Salary"};
		String[] choices = {Constants.CLERK, Constants.TELLER};
		Optional<ArrayList<String>> res = Dialogs.openEmployeeDialog(fields, fieldsHints, choices, "Add", "Add Employee");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			EmpManager empManagerInstance = EmpManager.getEmpManagerInstance();
			try {
				empManagerInstance.addNewEmployee(result.get(1), result.get(6), Float.parseFloat(result.get(4))
						, result.get(0), result.get(3), result.get(7), result.get(2), result.get(5));
				Alerts.createInfoAlert("Add Employee", "Employee was added successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void LoanRequest(ActionEvent event) {
		String[] fields = {"SSN", "Balance", "Loan Amount", "Loan Interest Rate"};
		String[] fieldsHints = {"Customer SSN", "Account Balance", "Loan Amount","Loan Interest Rate"};
		Optional<ArrayList<String>> result1 = Dialogs.openFieldsDialog(fields, fieldsHints, "Due Date", "Add", "Add Savings Account");
		if(result1.isPresent())
		{
			ArrayList<String> accResult = result1.get();
			AccountManager accountManagerInstance = AccountManager.getAccountManagerInstance();
			try {
				int AccNum = accountManagerInstance.addNewAccount(Float.parseFloat(accResult.get(1)), accResult.get(0), 
						"1", Globals.BranchID, Float.parseFloat(accResult.get(3)));
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
								"1", Globals.BranchID, Float.parseFloat(accResult.get(2)));
					}
				}
				LoansManager loanManagerInstance = LoansManager.getLoansManagerInstance();
				int loanId = loanManagerInstance.addNewLoan(AccNum, Float.parseFloat(accResult.get(2))
						, Float.parseFloat(accResult.get(3)), accResult.get(4));
				
				Alerts.createInfoAlert("Add Loan Account", "Loan Account was added successfully"
						, "Your Account Number is "+AccNum+" And your Loan ID is "+loanId);
				
			} catch (Exception e) {
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
