package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import Bank.ATMs;
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
		String[] selection = {"Sex", "Male", "Female"};
		String date = "Employee Birthdate";
		String[] choices = {Constants.CLERK, Constants.TELLER};
		Optional<ArrayList<String>> res = Dialogs.openGeneralDialog(fields, fieldsHints, choices, "Employee Title", date, selection, "Add", "Add Employee");
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
	
	public void ChangeSalary(ActionEvent event) {
		String[] fields = {"SSN", "New Salary"};
		String[] fieldsHints = {"Employee SSN", "New Salary"};
		Optional<ArrayList<String>> res = Dialogs.openFieldsDialog(fields, fieldsHints, "", "Update", "Change Salary");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			EmpManager empManagerInstance = EmpManager.getEmpManagerInstance();
			try {
				boolean success = empManagerInstance.updateEmployeeSalery(result.get(0), Float.parseFloat(result.get(1)));
				if(success)
				{
					Alerts.createInfoAlert("Change Salary", "Employee Salary has been changed successfully");
				}
				else
				{
					Alerts.createWarningAlert("Change Salary", "Wrong Employee SSN");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void ViewEmployee(ActionEvent event) {
		String[] fields = {"SSN"};
		String[] fieldsHints = {"Employee SSN"};
		Optional<ArrayList<String>> res = Dialogs.openFieldsDialog(fields, fieldsHints, "", "View", "View Employee Info");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			EmpManager empManagerInstance = EmpManager.getEmpManagerInstance();
			try {
				ArrayList<String> info = empManagerInstance
						.viewFromEmployee(result.get(0));
				if(info.isEmpty())
				{
					Alerts.createWarningAlert("Employee Info", "Wrong Employee SSN");
				}
				else
				{
					Alerts.createInfoAlert(
							"Employee Info",
							"Name: " + info.get(3),
							"SSN:\t\t\t" + info.get(0) + "\n" + 
							"Job Title:\t\t" + info.get(1) + "\n" + 
							"Salery:\t\t" + info.get(2) + "\n" + 
							"Address:\t\t" + info.get(4) + "\n" + 
							"Birthdate:\t\t" + info.get(5) + "\n" + 
							"Phone:\t\t" + info.get(6) + "\n" + 
							"Sex:\t\t\t"	+ info.get(7));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void AddSupervisor(ActionEvent event) {
		String[] fields = {"Supervisor SSN", "Employee SSN"};
		String[] fieldsHints = {"Supervisor SSN", "Employee SSN"};
		Optional<ArrayList<String>> res = Dialogs.openFieldsDialog(fields, fieldsHints, "", "Add", "Add Supervisor");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			EmpManager empManagerInstance = EmpManager.getEmpManagerInstance();
			try {
				boolean success = empManagerInstance.AddSupervisor(result.get(1), result.get(0));
				if(success)
				{
					Alerts.createInfoAlert("Add Supervisor", "Operation Completed");
				}
				else
				{
					Alerts.createWarningAlert("Add Supervisor", "Wrong Employee or Supervisor SSN");
				}
			} catch (SQLException e) {
				Alerts.createWarningAlert("Add Supervisor", "Duplicate Entry");
				e.printStackTrace();
			}
		}
	}
	
	public void ViewSupervisors(ActionEvent event) {
		String[] fields = {"Name", "SSN"};
		String[] fieldsHints = {"Employee Name", "Employee SSN"};
		Optional<ArrayList<String>> res = Dialogs.openFieldsDialog(fields, fieldsHints, "", "View", "View Employee Supervisors");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			EmpManager empManagerInstance = EmpManager.getEmpManagerInstance();
			try {
				ArrayList<ArrayList<String>> array = empManagerInstance
						.Watchsupervisor(result.get(1), result.get(0));
				if(array.isEmpty())
				{
					Alerts.createWarningAlert("Employee Supervisors", "Wrong Employee SSN or Name");
				}
				else
				{
					String print = "";
					for (int i = 0; i < array.size(); i++) {
						print += "Supervisor " + String.valueOf(i+1) + ":\n"+
								"\tName:\t\t" + array.get(i).get(1) + "\n" +
								"\tSSN:\t\t\t" + array.get(i).get(0) + "\n" +  
								"\tPhone:\t\t" + array.get(i).get(2) + "\n"; 
					}
					Alerts.createInfoAlert(
							"Employee Supervisors",
							"Employee Name:\t" + result.get(0) + "\n" +
							"Employee SSN:\t" + result.get(1),
							print);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void LoanRequest(ActionEvent event) {
		String[] fields = {"SSN", "Balance", "Loan Amount", "Loan Interest Rate"};
		String[] fieldsHints = {"Customer SSN", "Account Balance", "Loan Amount","Loan Interest Rate"};
		Optional<ArrayList<String>> result1 = Dialogs.openFieldsDialog(fields, fieldsHints, "Due Date", "Add", "Add Loan");
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

	public void AddATM(ActionEvent event) {
		String[] fields = {"Cash", "Location"};
		String[] fieldsHints = {"ATM Cash", "ATM Location"};
		String[] selection = {"In Bank", "Yes", "No"};
		String[] choices = {};
		Optional<ArrayList<String>> res = Dialogs.openGeneralDialog(fields, fieldsHints, choices, "", "", selection, "Add", "Add ATM");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			try {
				ATMs atmManagerInstance = ATMs.getATMsManagerInstance();
				boolean inBank = result.get(2).equals("Yes");
				atmManagerInstance.addNewATM(Constants.BANKID, Float.parseFloat(result.get(0)), result.get(1), inBank);
				Alerts.createInfoAlert("Add ATM", "ATM was added successfully");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void ListATMs(ActionEvent event) {
		String[] fields = {"ID"};
		String[] fieldsHints = {"ATM ID"};
		Optional<ArrayList<String>> res = Dialogs.openOptionalDialog(fields, fieldsHints, "View All ATMS", "View ATM", "ATM Details");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			try {
				ATMs atmManagerInstance = ATMs.getATMsManagerInstance();
				String details[][] = atmManagerInstance.show_ATM_Info(Integer.parseInt(result.get(0)));
				String print = "";
				for(int i = 0; i < details.length; i++)
				{
					if(details[i][0] == null)
						break;
					if(details.length > 1)
						print += "ATM " + String.valueOf(i+1) + ":\n";
					print += "ATM ID:\t\t" + details[i][0] + "\n";
					print += "ATM Cash:\t" + details[i][1] + "\n";
					print += "ATM Loaction:\t" + details[i][2] + "\n";
					if(details[i][3].equals("0"))
						print += "ATM In Bank:\t" + "Yes" + "\n\n";
					else
						print += "ATM In Bank:\t" + "No" + "\n\n";
				}
				if(print.isEmpty())
				{
					Alerts.createWarningAlert("ATM Details", "Wrong ATM ID");
				}
				else
				{
					Alerts.createInfoAlert("ATM Details", "Details", print);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else
		{
			Alerts.createInfoAlert("ATM Details", "Wrong ATM ID");
		}
	}

	public void RemoveATM(ActionEvent event) {
		String[] fields = {"ID"};
		String[] fieldsHints = {"ATM ID"};
		Optional<ArrayList<String>> res = Dialogs.openFieldsDialog(fields, fieldsHints, "", "Remove", "Remove ATM");
		if(res.isPresent())
		{
			ArrayList<String> result = res.get();
			try {
				ATMs atmManagerInstance = ATMs.getATMsManagerInstance();
				atmManagerInstance.removeATM(Integer.parseInt(result.get(0)));
				Alerts.createInfoAlert("Remove ATM", "ATM was removed successfully");
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
