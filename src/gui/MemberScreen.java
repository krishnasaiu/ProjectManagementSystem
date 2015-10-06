package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import control.Controller;

/*	
 * 	MemberScreen Controller class
 *  Copyright: Copyright © 2015
 *
 * @author Harshini
 * @version 1
 */
public class MemberScreen implements Initializable{

	@FXML
	private Button btAddAffiliationMembers;
	@FXML
	private Button btRemoveAffiliationMembers;
	@FXML
	private Button btAddToListMembers;
	@FXML
	private Button btRemoveFromListMembers;

	@FXML
	private TextField tfNameMemberMembers;
	@FXML
	private ListView<String> lvMembers;
	@FXML
	private ListView<String> lvTeamsForMembers;
	@FXML
	private ListView<String> lvCurrentTeamForMember;

	@FXML
	private Label lbAvailableTeamsForMember;
	@FXML
	private Label lbTeamsForMember;

	Controller theController = Controller.getInstance();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> members = FXCollections.observableArrayList(theController.getMembers());
		lvMembers.setItems(members);
	}

	/*
	 * Checks the text field for every character typed with the existing list of members
	 */
	@FXML
	private void handleNewMember(){
		String member = tfNameMemberMembers.getText();
		if(!alreadyExists(theController.getMembers(), member))
			btAddToListMembers.setDisable(false);
		else
			btAddToListMembers.setDisable(true);
	}

	/*
	 * Checks if the member is already in the list
	 */
	private boolean alreadyExists(ArrayList<String> members, String member) {
		System.out.println(member);
		for(String name : members){
			if(name.equalsIgnoreCase(member))
				return true;
		}
		return false;
	}

	/*
	 * adds new member to the list if it is not already included
	 */
	@FXML
	private void handleAddToList(){
		String member = tfNameMemberMembers.getText();
		theController.saveNewMember(member);
		lvMembers.setItems(FXCollections.observableArrayList(theController.getMembers()));
		btAddToListMembers.setDisable(true);
		tfNameMemberMembers.clear();
	}

	/*
	 * loads lists of teams which included the selected member and teams which doesn't included member
	 */
	@FXML
	private void handleMembersList(){
		String member = lvMembers.getSelectionModel().getSelectedItem();
		System.out.println(member);
		btRemoveFromListMembers.setDisable(false);
		lbTeamsForMember.setText(member);
		lbAvailableTeamsForMember.setText(member);
		lvTeamsForMembers.setItems(FXCollections.observableArrayList(theController.getAvailableTeamsFor(member)));
		lvCurrentTeamForMember.setItems(FXCollections.observableArrayList(theController.getTeamsFor(member)));
	}

	/*
	 * Removes the selected member form the list
	 */
	@FXML
	private void handleRemoveFromList(){
		String member = lvMembers.getSelectionModel().getSelectedItem();
		theController.removeMember(member);
		lvMembers.setItems(FXCollections.observableArrayList(theController.getMembers()));
		btRemoveAffiliationMembers.setDisable(true);
		tfNameMemberMembers.setText(member);
		lbTeamsForMember.setText("");
		lbAvailableTeamsForMember.setText("");
	}
	
	/*
	 * Displays available teams for the member
	 */
	@FXML
	private void handleAvailableTeams(){
		btAddAffiliationMembers.setDisable(false);
		btRemoveAffiliationMembers.setDisable(true);
	}
	
	/*
	 * Displays current teams for the member
	 */
	@FXML
	private void handleCurrentTeams(){
		btAddAffiliationMembers.setDisable(true);
		btRemoveAffiliationMembers.setDisable(false);
	}
	
	/*
	 * Add team's Affiliation to the member
	 */
	@FXML
	private void handleAddAffiliation(){
		String member = lbAvailableTeamsForMember.getText();
		String team = lvTeamsForMembers.getSelectionModel().getSelectedItem();
		theController.addAffiliation(member, team);
		lvTeamsForMembers.setItems(FXCollections.observableArrayList(theController.getAvailableTeamsFor(member)));
		lvCurrentTeamForMember.setItems(FXCollections.observableArrayList(theController.getTeamsFor(member)));
		btAddAffiliationMembers.setDisable(true);
	}
	
	/*
	 * Removes team's Affiliation with the member
	 */
	@FXML
	private void handleRemoveAffiliation(){
		String member = lbTeamsForMember.getText();
		String team = lvCurrentTeamForMember.getSelectionModel().getSelectedItem();
		theController.removeAffiliation(member, team);
		lvTeamsForMembers.setItems(FXCollections.observableArrayList(theController.getAvailableTeamsFor(member)));
		lvCurrentTeamForMember.setItems(FXCollections.observableArrayList(theController.getTeamsFor(member)));
		btRemoveAffiliationMembers.setDisable(true);
	}
}
