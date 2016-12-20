package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Bank.BankManager;
import Bank.EmpManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import extras.Alerts;
import extras.Constants;
import extras.Globals;

public class MainController implements Initializable {
	
	@FXML
	private Label bank_name;
	
	@FXML
	private TextField user;
	
	@FXML
	private PasswordField pass;
	
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
			Globals.BranchID = empManagerInstance.checkEmpLogin(user.getText(), 
					pass.getText(), type.getValue());
			
			if(Globals.BranchID == -1)
			{
				Alerts.createWarningAlert("Wrong Employee Name, Password, or Title");
			}
			else
			{
				Globals.SessionESSN = pass.getText();
				// TODO
				if(type.getValue().equals(Constants.MANAGER))
				{
					openWindow("/ManagerWindow.fxml");
				}
				else if(type.getValue().equals(Constants.CLERK))
				{
					openWindow("/ClerkWindow.fxml");
				}
				else if(type.getValue().equals(Constants.TELLER))
				{
					openWindow("/TellerWindow.fxml");
				}
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		type.setItems(EmpTypes);
		BankManager bankInstance = BankManager.getBankManagerInstance();
		Globals.BankName = bankInstance.getBankName(Constants.BANKID);
		bank_name.setText(Globals.BankName);
	}
	
	private void openWindow(String fxmlFile)
	{
		Stage primaryStage = (Stage) user.getScene().getWindow();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource(fxmlFile));
			Scene scene = user.getScene();
			scene.setRoot(root);
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
