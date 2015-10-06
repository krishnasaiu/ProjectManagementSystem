package gui;

import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import persistence.DataBaseManager;
import model.ActionItem;
import control.Controller;

/*
 * ActionItemScreen Controller Class
 * Copyright: Copyright © 2015
 *
 * @author Krishna Sai, Kranthi, Harshini
 * @version 1
 */
public class ActionItemScreen implements Initializable{
	@FXML
	private ComboBox<ActionItem> cbActionItems;

	@FXML
	private ComboBox<String> cbSortingDirectionActionItems;
	@FXML
	private ComboBox<String> cbFirstSortingOrderActionItems;
	@FXML 
	private ComboBox<String> cbSecondSortingOrderActionItems;
	@FXML 
	private ComboBox<String> cbInclusionFactorActionItems;

	@FXML
	private TextField tfSelActItemsNameActionItems;
	@FXML
	private TextArea taDescriptionActionItems;
	@FXML 
	private TextArea taResolutionActionItems;

	@FXML
	private Label lbCreationActionItems;
	@FXML
	private TextField tfDueActionItems;
	@FXML 
	private ComboBox<String> cbStatusActionItems;
	@FXML 
	private ComboBox<String> cbAssignMemActionItems;
	@FXML 
	private ComboBox<String> cbAssignTeamActionItems;

	@FXML
	private Button btUpdateActionItemActionItems;
	@FXML
	private Button btClearFormActionItems;
	@FXML
	private Button btCreateActionItemActionItems;
	@FXML
	private Button btDeleteActionItemActionItems;

	@FXML
	private Label lbUnsavedChanges	;

	private int direction;
	private int firstOrder;
	private int secondOrder;
	private int inclusionFactor;
	private final ActionItem divider = new ActionItem("------------------------");
	private final ActionItem noSelection = new ActionItem("- no country selected - ");
	private Controller theController = null;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	/*
	 * Initial state of the comboBox is 0
	 */
	private int state;
	ObservableList<ActionItem> priorityList = FXCollections.observableArrayList();
	ArrayList<ActionItem> actionItems;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		theController = Controller.getInstance();

		cbSortingDirectionActionItems.getSelectionModel().selectFirst();

		actionItems = theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor);
		cbActionItems.setItems(FXCollections.observableArrayList(actionItems));

		ObservableList<String> members = FXCollections.observableArrayList(theController.getMembers());
		cbAssignMemActionItems.setItems(members);

		ObservableList<String> teams = FXCollections.observableArrayList(theController.getTeams());
		cbAssignTeamActionItems.setItems(teams);

		cbStatusActionItems.getSelectionModel().selectFirst();
		
		direction = 0;
		firstOrder = 0;
		secondOrder = 0;
		inclusionFactor = 0;
	} 
	
	//	Handles ActionItem selection ComboBox
	@FXML
	private void handleActionItemSelection(){
		btUpdateActionItemActionItems.setDisable(false);
		btDeleteActionItemActionItems.setDisable(false);
		
		ObservableList<ActionItem> actionItems = FXCollections.observableArrayList(theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor));
		ActionItem ai = cbActionItems.getValue();
		System.out.println("ai: "+ai);
		if(ai != null && !ai.equals(divider)){
			tfSelActItemsNameActionItems.setText(ai.getActionItemName());
			taDescriptionActionItems.setText(ai.getDescription());
			taResolutionActionItems.setText(ai.getResolution());
			lbCreationActionItems.setText(ai.getCreatedDate().toString());
			tfDueActionItems.setText(ai.getDueDate().toString());
			cbStatusActionItems.getSelectionModel().select(ai.getStatus());
			cbAssignMemActionItems.getSelectionModel().select(ai.getAssignedMember());
			cbAssignTeamActionItems.getSelectionModel().select(ai.getAssignedTeam());
		}
		else{
			clearForm();
		}
		
		if (ai != null){
			/*
			 * if "- no country selected - " is selected comboBox goes to default state
			 */
			if((ai.equals(noSelection) && state>0) || ai.equals(divider)){
				priorityList.clear();
				state = 0;
			}
			/*
			 * select labels to their respective values
			 * and perform changes to comboBox
			 */
			else if(!ai.equals(noSelection)){
				if(priorityList.contains(ai))
					priorityList.remove(ai);
				
				if(state == 0)
					priorityList.add(divider);
				priorityList.add(0, ai);
				
				if(priorityList.size() > 4)
					priorityList.remove(3);
				state++;
			}
		}
		cbActionItems.setItems(concatLists(priorityList, actionItems));
	}
	
	/*
	 * returns concantenated observable list of the two observable lists 
	 * passed as arguements 
	 */
	private ObservableList<ActionItem> concatLists(
			ObservableList<ActionItem> priorityList,
			ObservableList<ActionItem> countries) {
		ObservableList<ActionItem> temp = FXCollections.observableArrayList();
		if(priorityList != null)
			temp.addAll(priorityList);
		temp.addAll(countries);
		return temp;
	}
	
	//	Handles ActionItem create action item button
	@FXML
	private void handleCreateNewActionItem() throws ParseException{
		String name = tfSelActItemsNameActionItems.getText();
		String description = taDescriptionActionItems.getText();
		String resolution = taResolutionActionItems.getText();
		String d = tfDueActionItems.getText();
		Calendar c = GregorianCalendar.getInstance();
		java.sql.Date creationDate = new Date(c.getTime().getTime());
		java.sql.Date dueDate = new Date(dateFormat.parse(d).getTime()); 
		String status = cbStatusActionItems.getValue();
		String atm = cbAssignMemActionItems.getValue();
		String att = cbAssignTeamActionItems.getValue();

		ActionItem ai = null;
		try {
			ai = new ActionItem(name, description, resolution, creationDate, dueDate, status, atm, att);
			DataBaseManager.writeActionItem(ai);
			clearForm();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("Error!");
			alert.setContentText(e.getMessage());
			alert.showAndWait();

			e.printStackTrace();
			return;
		}
	}

	/**
	 * Update the current action item in memory
	 * 
	 * @throws ParseException 
	 */
	@FXML
	private void handleUpdateActionItem() throws ParseException {
		String name = tfSelActItemsNameActionItems.getText();
		String description = taDescriptionActionItems.getText();
		String resolution = taResolutionActionItems.getText();
		String d = tfDueActionItems.getText();
		Calendar c = GregorianCalendar.getInstance();
		java.sql.Date creationDate = new Date(c.getTime().getTime());
		java.sql.Date dueDate = new Date(dateFormat.parse(d).getTime()); 
		String status = cbStatusActionItems.getValue();
		String atm = cbAssignMemActionItems.getValue();
		String att = cbAssignTeamActionItems.getValue();

		// Tell the ActionItemManager to save the update
		ActionItem ai = null;
		try {
			int index = theController.getSerialNumber(cbActionItems.getValue());
			ai = new ActionItem(name, description, resolution, creationDate, dueDate, status, atm, att);
			if(!theController.inUse(index)){
				theController.setInUse(index, true);
				theController.saveActionItem(index, ai);
				theController.setInUse(index, false);
				cbActionItems.setItems(FXCollections.observableArrayList(theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor)));
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Access Denied!");
				alert.setHeaderText("Access Denied!");
				alert.setContentText("Action Item is being updated by someone else\n Please wait and try later");
				alert.showAndWait();
			}
		}
		catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("Error!");
			alert.setContentText(e.getMessage());
			alert.showAndWait();

			e.printStackTrace();
			return;
		}
	}

	/**
	 * Delete the current action item in memory
	 * 
	 * @throws ParseException 
	 */
	@FXML
	private void handleDeleteActionItem() throws ParseException {

		// Tell the ActionItemManager to save the update
		try {
			int index = theController.getSerialNumber(cbActionItems.getValue());
			theController.deleteActionItem(index);
			cbActionItems.setItems(FXCollections.observableArrayList(theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor)));
			clearForm();
		}
		catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("Error!");
			alert.setContentText(e.getMessage());
			alert.showAndWait();

			e.printStackTrace();
			return;
		}
	}
	
	/*
	 *	Clears the form 
	 */
	@FXML
	private void handleClearForm(){
		clearForm();
	}

	private void clearForm(){
		direction = 0;
		firstOrder = 0;
		secondOrder = 0;
		inclusionFactor = 0;
		cbSortingDirectionActionItems.getSelectionModel().selectFirst();
		ObservableList<ActionItem> actionItems = FXCollections.observableArrayList(theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor));
		cbActionItems.setItems(actionItems);

		ObservableList<String> members = FXCollections.observableArrayList(theController.getMembers());
		cbAssignMemActionItems.setItems(members);

		ObservableList<String> teams = FXCollections.observableArrayList(theController.getTeams());
		cbAssignTeamActionItems.setItems(teams);

		tfSelActItemsNameActionItems.clear();
		taDescriptionActionItems.clear();
		taResolutionActionItems.clear();
		tfDueActionItems.clear();

		btUpdateActionItemActionItems.setDisable(true);
		btDeleteActionItemActionItems.setDisable(true);
	}
	
	/*
	 * sorts the list based on the option selected
	 */
	@FXML
	private void handleSortingDirection(){
		String d = cbSortingDirectionActionItems.getValue();
		if(d.equals("Large to Small"))
			direction = 0;
		else
			direction = 1;
		
		actionItems = theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor);
		cbActionItems.setItems(FXCollections.observableArrayList(actionItems));
	}

	@FXML
	private void handleFirstSorting(){
		String o = cbFirstSortingOrderActionItems.getValue();
		if(o.equals("None"))
			firstOrder = 0;
		else if(o.equals("Creation Date"))
			firstOrder = 1;
		else if(o.equals("Due Date"))
			firstOrder = 2;
		else if(o.equals("Assigned Member"))
			firstOrder = 3;
		else
			firstOrder = 4;
		
		actionItems = theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor);
		cbActionItems.setItems(FXCollections.observableArrayList(actionItems));
	}

	@FXML
	private void handleSecondSorting(){
		String o = cbSecondSortingOrderActionItems.getValue();
		if(o.equals("None"))
			secondOrder = 0;
		else if(o.equals("Creation Date"))
			secondOrder = 1;
		else if(o.equals("Due Date"))
			secondOrder = 2;
		else if(o.equals("Assigned Member"))
			secondOrder = 3;
		else
			secondOrder = 4;
		
		actionItems = theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor);
		cbActionItems.setItems(FXCollections.observableArrayList(actionItems));
	}

	@FXML
	private void handleInclusionFactor(){
		String i = cbInclusionFactorActionItems.getValue();
		if(i.equals("All Action Items"))
			inclusionFactor = 0;
		else if(i.equals("Open Action Items"))
			inclusionFactor = 1;
		else 
			inclusionFactor = 2;
		
		actionItems = theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor);
		cbActionItems.setItems(FXCollections.observableArrayList(actionItems));
	}
}
