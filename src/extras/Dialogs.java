package extras;

import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

public class Dialogs {
	
	public static Optional<ArrayList<String>> openFieldsDialog(String[] fields, String[] fieldsHints, String buttonLabel, String title) {
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
		
		for(int i = 0; i < size; i++)
		{
			textFields.get(i).textProperty().addListener((observable, oldValue, newValue) -> {
				addButton.setDisable(false);
				for(int j = 0; j < size; j++)
				{
					if(textFields.get(j).getText().isEmpty())
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
}
