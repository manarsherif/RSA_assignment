package extras;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class Dialogs {
	
	public static Optional<ArrayList<String>> openFieldsDialog(String[] fields, String[] fieldsHints, String dateField, String buttonLabel, String title) {
		Dialog<ArrayList<String>> dialog = new Dialog<>();
		dialog.setTitle(title);
		ButtonType button = new ButtonType(buttonLabel, ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(button, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(40, 150, 30, 30));
		
		ArrayList<TextField> textFields = new ArrayList<>();
		int size = fields.length;
		for(int i = 0; i < size; i++)
		{
			textFields.add(new TextField());
			textFields.get(i).setPromptText(fieldsHints[i]);
			
			grid.add(new Label(fields[i]+":"), 0, i);
			grid.add(textFields.get(i), 1, i);
		}
		
		// Enable/Disable add button depending on whether fields were entered.
		Node addButton = dialog.getDialogPane().lookupButton(button);
		addButton.setDisable(true);
		
		DatePicker datePicker = new DatePicker();
		if(!dateField.isEmpty())
		{
			datePicker.setPromptText(dateField);
			grid.add(new Label(dateField+":"), 0, textFields.size()+1);
			grid.add(datePicker, 1, textFields.size()+1);
			
			datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
				addButton.setDisable(false);
				for(int j = 0; j < size; j++)
				{
					if(textFields.get(j).getText().isEmpty() || datePicker.getValue() == null)
					{
						addButton.setDisable(true);
					}
				}
			});
		}
		
		for(int i = 0; i < size; i++)
		{
			textFields.get(i).textProperty().addListener((observable, oldValue, newValue) -> {
				addButton.setDisable(false);
				for(int j = 0; j < size; j++)
				{
					if(textFields.get(j).getText().isEmpty() 
							|| (!dateField.isEmpty() && datePicker.getValue() == null))
					{
						addButton.setDisable(true);
					}
				}
			});
		}
		
		dialog.getDialogPane().setContent(grid);

		// Request focus on the name field by default.
		Platform.runLater(() -> textFields.get(0).requestFocus());
		

		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == button) {
		    	ArrayList<String> result = new ArrayList<>();
		    	for(int i = 0; i < size; i++)
		    	{
		    		result.add(textFields.get(i).getText());
		    	}
		    	if(!dateField.isEmpty())
		    	{
		    		Instant instant = Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()));
			    	Date date = Date.from(instant);
			    	String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
		    		result.add(dateString);
		    	}
		        return result;
		    }
		    return null;
		});
		
		Optional<ArrayList<String>> result = dialog.showAndWait();
		return result;
	}
	
	public static Optional<ArrayList<String>> openUpdateDialog(String[] fields, String[] hints, String[] Choices, String buttonLabel, String title) {
		Dialog<ArrayList<String>> dialog = new Dialog<>();
		dialog.setTitle(title);
		ButtonType button = new ButtonType(buttonLabel, ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(button, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(40, 150, 30, 30));
		
		ArrayList<TextField> textFields = new ArrayList<>();
		for(int i = 0; i < fields.length; i++)
		{
			textFields.add(new TextField());
			textFields.get(i).setPromptText(hints[i]);
			
			grid.add(new Label(fields[i]+":"), 0, i);
			grid.add(textFields.get(i), 1, i);
		}
		
		TextField textField = new TextField();
		textFields.add(textField);
		ComboBox<String> choicesBox = new ComboBox<>();
		choicesBox.setPromptText("Choose a Field to Update");
		
		ObservableList<String> list = FXCollections.observableArrayList(Choices);
		choicesBox.setItems(list);
		grid.add(choicesBox, 0, textFields.size()-1);
		grid.add(textField, 1, textFields.size()-1);
		
		// Enable/Disable add button depending on whether fields were entered.
		Node addButton = dialog.getDialogPane().lookupButton(button);
		addButton.setDisable(true);
		
		for(int i = 0; i < textFields.size(); i++)
		{
			textFields.get(i).textProperty().addListener((observable, oldValue, newValue) -> {
				addButton.setDisable(false);
				for(int j = 0; j < textFields.size(); j++)
				{
					if(textFields.get(j).getText().isEmpty() || choicesBox.getSelectionModel().getSelectedIndex() < 0)
					{
						addButton.setDisable(true);
					}
				}
			});
		}
		
		choicesBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			addButton.setDisable(false);
			for(int j = 0; j < textFields.size(); j++)
			{
				if(textFields.get(j).getText().isEmpty() || choicesBox.getSelectionModel().getSelectedIndex() < 0)
				{
					addButton.setDisable(true);
				}
			}
		});
		
		dialog.getDialogPane().setContent(grid);

		// Request focus on the name field by default.
		Platform.runLater(() -> textField.requestFocus());
		

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == button) {
		    	ArrayList<String> result = new ArrayList<>();
		    	for(int i = 0; i < textFields.size(); i++)
		    	{
		    		result.add(textFields.get(i).getText());
		    	}
		    	result.add(choicesBox.getValue());
		        return result;
		    }
		    return null;
		});
		
		Optional<ArrayList<String>> result = dialog.showAndWait();
		return result;
	}
	
	public static Optional<ArrayList<String>> openGeneralDialog(String[] fields, String[] hints, String[] Choices, String ChoicesTitle, String dateLabel, String[] selectionLabel, String buttonLabel, String title) {
		Dialog<ArrayList<String>> dialog = new Dialog<>();
		dialog.setTitle(title);
		ButtonType button = new ButtonType(buttonLabel, ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(button, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(40, 150, 30, 30));
		
		ArrayList<TextField> textFields = new ArrayList<>();
		for(int i = 0; i < fields.length; i++)
		{
			textFields.add(new TextField());
			textFields.get(i).setPromptText(hints[i]);
			
			grid.add(new Label(fields[i]+":"), 0, i);
			grid.add(textFields.get(i), 1, i);
		}
		DatePicker BDPicker = new DatePicker();
		
		ToggleGroup group = new ToggleGroup();
		if(selectionLabel.length != 0)
		{
			grid.add(new Label(selectionLabel[0]), 0, textFields.size()+1);
			for(int i = 1; i < selectionLabel.length; i++)
			{
				RadioButton choice = new RadioButton(selectionLabel[i]);
				choice.setToggleGroup(group);
				grid.add(choice, i, textFields.size()+1);
				if(i == 1)
					choice.setSelected(true);
			}
		}
		
		if(!dateLabel.isEmpty()) 
		{
			
			BDPicker.setPromptText(dateLabel);
			
			grid.add(new Label(dateLabel+":"), 0, textFields.size()+2);
			grid.add(BDPicker, 1, textFields.size()+2);
		}		
		
		// Enable/Disable add button depending on whether fields were entered.
		Node addButton = dialog.getDialogPane().lookupButton(button);
		addButton.setDisable(true);
		
		ComboBox<String> choicesBox = new ComboBox<>();
		
		if(Choices.length != 0)
		{
			choicesBox.setPromptText(ChoicesTitle);		
			ObservableList<String> list = FXCollections.observableArrayList(Choices);
			choicesBox.setItems(list);
			grid.add(new Label(ChoicesTitle), 0, textFields.size()+3);
			grid.add(choicesBox, 1, textFields.size()+3);
			
			choicesBox.valueProperty().addListener((observable, oldValue, newValue) -> {
				addButton.setDisable(false);
				for(int j = 0; j < textFields.size(); j++)
				{
					if(textFields.get(j).getText().isEmpty() || choicesBox.getSelectionModel().getSelectedIndex() < 0)
					{
						addButton.setDisable(true);
					}
				}
			});
		}
		
		
		for(int i = 0; i < textFields.size(); i++)
		{
			textFields.get(i).textProperty().addListener((observable, oldValue, newValue) -> {
				addButton.setDisable(false);
				for(int j = 0; j < textFields.size(); j++)
				{
					if(textFields.get(j).getText().isEmpty() ||( Choices.length != 0 && choicesBox.getSelectionModel().getSelectedIndex() < 0))
					{
						addButton.setDisable(true);
					}
				}
			});
		}
		
		dialog.getDialogPane().setContent(grid);

		// Request focus on the name field by default.
		Platform.runLater(() -> textFields.get(0).requestFocus());
		

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == button) {
		    	ArrayList<String> result = new ArrayList<>();
		    	for(int i = 0; i < textFields.size(); i++)
		    	{
		    		result.add(textFields.get(i).getText());
		    	}
		    	String dateString = "";
		    	
		    	if(selectionLabel.length != 0)
		    	{
			    	String selection = ((RadioButton)group.getSelectedToggle()).getText();
			    	result.add(selection);
		    	}
		    	if(Choices.length != 0)
		    	{
			    	result.add(choicesBox.getValue());
		    	}
		    	if(BDPicker.getValue() != null)
		    	{
		    		Instant instant = Instant.from(BDPicker.getValue().atStartOfDay(ZoneId.systemDefault()));
			    	Date date = Date.from(instant);
			    	dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
		    	}
		    	result.add(dateString);
		        return result;
		    }
		    return null;
		});
		
		Optional<ArrayList<String>> result = dialog.showAndWait();
		return result;
	}
}
