package application;

import java.net.URL;
import java.util.ResourceBundle;

import Bank.BankManager;
import Bank.EmpManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import extras.Alerts;
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
		if(user.getText().isEmpty())
		{
			Alerts.createWarningAlert("Please Enter Your Name");
		}
		else if(pass.getText().isEmpty())
		{
			Alerts.createWarningAlert("Please Enter Your Password");
		}
		else if(type.getSelectionModel().getSelectedIndex() < 0)
		{
			Alerts.createWarningAlert("Please Enter Your Job Title");
		}
		else
		{
			EmpManager empManagerInstance = EmpManager.getEmpManagerInstance();
			boolean loginSuccess = empManagerInstance.checkEmpLogin(user.getText(), 
					pass.getText(), type.getValue());
			
			if(loginSuccess == false)
			{
				Alerts.createWarningAlert("Wrong Employee Name, Password, or Title");
			}
			else
			{
				// TODO
			}
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		type.setItems(EmpTypes);
		BankManager bankInstance = BankManager.getBankManagerInstance();
		String BankName = bankInstance.getBankName(Constants.BANKID);
		bank_name.setText(BankName);
	}
}
