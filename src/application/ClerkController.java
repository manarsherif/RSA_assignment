package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import Bank.AccountManager;
import Bank.CustomerManager;
import extras.Alerts;
import extras.Globals;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClerkController implements Initializable {
	
	@FXML
	private Label bank_name;
	
	public void AddAccount(ActionEvent event) {
		
	}
	
	public void AddCustomer(ActionEvent event) {
		openCustomerDialog();
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
	
	private void openCustomerDialog() {
		Dialog<ArrayList<String>> dialog = new Dialog<>();
		dialog.setTitle("Add Customer");
		ButtonType button = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(button, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(40, 150, 30, 30));
		
		TextField name = new TextField();
		name.setPromptText("Customer Name");
		TextField ssn = new TextField();
		ssn.setPromptText("Customer SSN");
		TextField phone = new TextField();
		phone.setPromptText("Customer Phone");
		TextField address = new TextField();
		address.setPromptText("Customer Address");
		
		grid.add(new Label("Name:"), 0, 0);
		grid.add(name, 1, 0);
		grid.add(new Label("SSN:"), 0, 1);
		grid.add(ssn, 1, 1);
		grid.add(new Label("Phone:"), 0, 2);
		grid.add(phone, 1, 2);
		grid.add(new Label("Address:"), 0, 3);
		grid.add(address, 1, 3);
		
		// Enable/Disable add button depending on whether fields were entered.
		Node addButton = dialog.getDialogPane().lookupButton(button);
		addButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		name.textProperty().addListener((observable, oldValue, newValue) -> {
			if(name.getText().isEmpty() || ssn.getText().isEmpty() 
					|| phone.getText().isEmpty() || address.getText().isEmpty())
			{
				addButton.setDisable(true);
			}
			else
			{
				addButton.setDisable(false);
			}
		});
		
		ssn.textProperty().addListener((observable, oldValue, newValue) -> {
			if(name.getText().isEmpty() || ssn.getText().isEmpty() 
					|| phone.getText().isEmpty() || address.getText().isEmpty())
			{
				addButton.setDisable(true);
			}
			else
			{
				addButton.setDisable(false);
			}
		});
		
		phone.textProperty().addListener((observable, oldValue, newValue) -> {
			if(name.getText().isEmpty() || ssn.getText().isEmpty() 
					|| phone.getText().isEmpty() || address.getText().isEmpty())
			{
				addButton.setDisable(true);
			}
			else
			{
				addButton.setDisable(false);
			}
		});
		
		address.textProperty().addListener((observable, oldValue, newValue) -> {
			if(name.getText().isEmpty() || ssn.getText().isEmpty() 
					|| phone.getText().isEmpty() || address.getText().isEmpty())
			{
				addButton.setDisable(true);
			}
			else
			{
				addButton.setDisable(false);
			}
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the name field by default.
		Platform.runLater(() -> name.requestFocus());
		

		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == button) {
		    	ArrayList<String> result = new ArrayList<>();
		    	result.add(name.getText());
		    	result.add(ssn.getText());
		    	result.add(phone.getText());
		    	result.add(address.getText());
		        return result;
		    }
		    return null;
		});
		
		Optional<ArrayList<String>> result = dialog.showAndWait();
		
		result.ifPresent( inputs -> {
			CustomerManager customerManagerInstance = CustomerManager.getCustomerManagerInstance();
			try {
				customerManagerInstance.addNewCustomer(inputs.get(1), inputs.get(0), inputs.get(2), inputs.get(3));
				Alerts.createInfoAlert("Add Customer", "Customer were added successfully");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
