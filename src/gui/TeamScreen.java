package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/*	
 * 	TeamScreen Controller class
 *  Copyright: Copyright © 2015
 *
 * @author Harshini
 * @version 1
 */

public class TeamScreen implements Initializable{

	@FXML
	private Button btAddToListTeams;
	@FXML
	private Button btRemoveFromListTeams;
	@FXML
	private Button btAddAssociationTeams;
	@FXML
	private Button btRemoveAssociationTeams;

	@FXML
	private TextField tfNewTeamTeams;
	@FXML
	private ListView<String> lvTeams;
	@FXML
	private ListView<String> lvMembersForTeam;
	@FXML
	private ListView<String> lvCurrentMembers;
	
	@FXML
	private Label lbAvailableMembersForTeam;
	@FXML
	private Label lbMembersOfTeam;

	Controller theController = Controller.getInstance();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> teams = FXCollections.observableArrayList(theController.getTeams());
		lvTeams.setItems(teams);
	}

	/*
	 * Checks the text field for every character typed with the existing list of teams
	 */
	@FXML
	private void handleNewTeam(){
		String team = tfNewTeamTeams.getText();
		System.out.println(team);
		if(!alreadyExists(theController.getTeams(), team))
			btAddToListTeams.setDisable(false);
		else
			btAddToListTeams.setDisable(true);
	}

	/*
	 * Checks if the team is already in the list
	 */
	private boolean alreadyExists(ArrayList<String> teams, String team) {
		System.out.println(team);
		for(String name : teams){
			if(name.equalsIgnoreCase(team))
				return true;
		}
		return false;
	}

	/*
	 * adds new team to the list if it is not already included
	 */
	@FXML
	private void handleAddToList(){
		String team = tfNewTeamTeams.getText();
		theController.saveNewTeam(team);
		lvTeams.setItems(FXCollections.observableArrayList(theController.getTeams()));
		btAddToListTeams.setDisable(true);
		tfNewTeamTeams.clear();
	}

	/*
	 * loads lists of members which are affiliated to team and which are not.
	 */
	@FXML
	private void handleMembersList(){
		String team = lvTeams.getSelectionModel().getSelectedItem();
		System.out.println(team);
		btRemoveFromListTeams.setDisable(false);
		lbMembersOfTeam.setText(team);
		lbAvailableMembersForTeam.setText(team);
		lvMembersForTeam.setItems(FXCollections.observableArrayList(theController.getAvailableMembersFor(team)));
		lvCurrentMembers.setItems(FXCollections.observableArrayList(theController.getMembersFor(team)));
	}

	/*
	 * Removes the selected team form the list
	 */
	@FXML
	private void handleRemoveFromList(){
		String team = lvTeams.getSelectionModel().getSelectedItem();
		theController.removeTeam(team);
		lvTeams.setItems(FXCollections.observableArrayList(theController.getTeams()));
		btRemoveFromListTeams.setDisable(true);
		tfNewTeamTeams.setText(team);
		lbMembersOfTeam.setText("");
		lbAvailableMembersForTeam.setText("");
	}
	
	/*
	 * Displays available members for the team
	 */
	@FXML
	private void handleAvailableMembers(){
		btAddAssociationTeams.setDisable(false);
		btRemoveAssociationTeams.setDisable(true);
	}
	
	/*
	 * Displays current members for the team
	 */
	@FXML
	private void handleCurrentMembers(){
		btAddAssociationTeams.setDisable(true);
		btRemoveAssociationTeams.setDisable(false);
	}
	
	/*
	 * Adds member's association with the team
	 */
	@FXML
	private void handleAddAssociation(){
		String team =lbAvailableMembersForTeam.getText();
		String member = lvMembersForTeam.getSelectionModel().getSelectedItem();
		theController.addAffiliation(member, team);
		lvMembersForTeam.setItems(FXCollections.observableArrayList(theController.getAvailableMembersFor(member)));
		lvCurrentMembers.setItems(FXCollections.observableArrayList(theController.getMembersFor(member)));
		btAddAssociationTeams.setDisable(true);
	}
	
	/*
	 * Removes member's association with the team
	 */
	@FXML
	private void handleRemoveAssociation(){
		String team = lbMembersOfTeam.getText();
		String member = lvCurrentMembers.getSelectionModel().getSelectedItem();
		theController.removeAffiliation(member, team);
		lvMembersForTeam.setItems(FXCollections.observableArrayList(theController.getAvailableMembersFor(member)));
		lvCurrentMembers.setItems(FXCollections.observableArrayList(theController.getMembersFor(member)));
		btRemoveAssociationTeams.setDisable(true);
	}
}
