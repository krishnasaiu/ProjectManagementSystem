package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.ActionItem;
import control.Controller;

/*
 * ConsoleScreen Controller Class
 * 
 * Copyright: Copyright © 2015
 *
 * @author Krishna Sai
 * @version 1
 */

public class ConsoleScreen implements Initializable{
	@FXML
	private ListView<ActionItem> lvActionItemsConsole;
	@FXML
	private TextField tfNameConsole;
	@FXML
	private TextArea taDescriptionConsole;
	@FXML 
	private TextArea taResolutionConsole;

	@FXML
	private ComboBox<String> cbSortingDirectionConsole;
	@FXML
	private ComboBox<String> cbFirstSortingOrderConsole;
	@FXML 
	private ComboBox<String> cbSecondSortingOrderConsole;
	@FXML 
	private ComboBox<String> cbInclusionFactorConsole;

	@FXML
	private Label lbCreationDateConsole;
	@FXML
	private Label lbDueDateConsole;
	@FXML
	private Label lbStatusConsole;
	@FXML
	private Label lbAssignMemConsole;
	@FXML
	private Label lbAssignTeamConsole;
	@FXML
	private Label lbInternetConsole;

	private int direction;
	private int firstOrder;
	private int secondOrder;
	private int inclusionFactor;
	Controller theController = Controller.getInstance();
	private ArrayList<ActionItem> actionItems;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cbSortingDirectionConsole.getSelectionModel().selectFirst();
		System.out.println(cbSortingDirectionConsole.getValue());
		actionItems = theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor);
		lvActionItemsConsole.setItems(FXCollections.observableArrayList(actionItems));
		
		direction = 0;
		firstOrder = 0;
		secondOrder = 0;
		inclusionFactor = 0;
	}

	/*
	 * Sets the selected Actionitem's details to the fields from the list view
	 */
	@FXML
	private void handleActionItemsListView(){
		ActionItem ai = lvActionItemsConsole.getSelectionModel().getSelectedItem();
		tfNameConsole.setText(ai.getActionItemName());
		taDescriptionConsole.setText(ai.getDescription());
		taResolutionConsole.setText(ai.getResolution());
		lbCreationDateConsole.setText(ai.getCreatedDate().toString());
		lbDueDateConsole.setText(ai.getDueDate().toString());
		lbStatusConsole.setText(ai.getStatus());
		lbAssignMemConsole.setText(ai.getAssignedMember());
		lbAssignTeamConsole.setText(ai.getAssignedTeam());
	}

	/*
	 * sorts the list based on the option selected
	 */
	@FXML
	private void handleSortingDirection(){
		String d = cbSortingDirectionConsole.getValue();
		if(d.equals("Small to Large"))
			direction = 0;
		else
			direction = 1;
		
		actionItems = theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor);
		lvActionItemsConsole.setItems(FXCollections.observableArrayList(actionItems));
	}

	@FXML
	private void handleFirstSorting(){
		String o = cbFirstSortingOrderConsole.getValue();
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
		lvActionItemsConsole.setItems(FXCollections.observableArrayList(actionItems));
	}

	@FXML
	private void handleSecondSorting(){
		String o = cbSecondSortingOrderConsole.getValue();
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
		lvActionItemsConsole.setItems(FXCollections.observableArrayList(actionItems));
	}

	@FXML
	private void handleInclusionFactor(){
		String i = cbInclusionFactorConsole.getValue();
		if(i.equals("All Action Items"))
			inclusionFactor = 0;
		else if(i.equals("Open Action Items"))
			inclusionFactor = 1;
		else 
			inclusionFactor = 2;
		
		actionItems = theController.getActionItems(direction, firstOrder, secondOrder, inclusionFactor);
		lvActionItemsConsole.setItems(FXCollections.observableArrayList(actionItems));
	}
}
