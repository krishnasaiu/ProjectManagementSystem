package control;

import java.util.ArrayList;

import model.ActionItem;
import persistence.DataBaseManager;

/**
 * <p>
 * Title: Controller
 * </p>
 *
 * <p>
 * Description: A singleton class to control instances of the assorted managers
 * </p>
 *
 * <p>
 * Copyright: Copyright © 2015
 * </p>
 *
 * @author Krishna Sai, Kranthi, Harshini
 * @version 1
 */
public class Controller {
	//---------------------------------------------------------------------------------------------------------------------
	// Controller Attributes

	private static Controller theController = null;
	//---------------------------------------------------------------------------------------------------------------------

	/*
      This constructor is private and synchronized because we are using the singleton design pattern and we want only one!
	 */
	private Controller() {
	}

	/**
	 * Return the singleton instance of the Controller class
	 * @return Controller
	 */
	public static synchronized Controller getInstance() { 
		if(theController == null){
			theController = new Controller();
		}
		return theController; }

	public int getSerialNumber(ActionItem ai) {
		return DataBaseManager.getSerialNumber(ai);
	}
	
	public ArrayList<ActionItem> getActionItems(int direction, int firstOrder, int secondOrder, int inclusionFactor){
		return DataBaseManager.getActionItems(direction, firstOrder, secondOrder, inclusionFactor);
	}
	
	public ActionItem getSelectedActionItem(String aiName){
		return DataBaseManager.getSelectedActionItem(aiName);
	}
	
	public void saveActionItem(ActionItem ai){
		DataBaseManager.writeActionItem(ai);
	}
	
	public void saveActionItem(int index, ActionItem ai){
		DataBaseManager.writeActionItem(index, ai);
	}
	
	public void deleteActionItem(int index){
		DataBaseManager.deleteActionItem(index);
	}
	
	public ArrayList<String> getMembers(){
		return DataBaseManager.getMembers();
	}
	
	public ArrayList<String> getTeams(){
		return DataBaseManager.getTeams();
	}
	
	public String isValidUser(String username, String password){
		return DataBaseManager.connection(username, password);
	}
	
	public void saveNewMember(String member) {
		DataBaseManager.saveNewMember(member);
	}
	
	public void removeMember(String member) {
		DataBaseManager.removeMember(member);
	}
	
	public void saveNewTeam(String team) {
		DataBaseManager.saveNewTeam(team);
	}
	
	public void removeTeam(String team) {
		DataBaseManager.removeTeam(team);
	}
	
	public ArrayList<String> getAvailableTeamsFor(String member) {
		return DataBaseManager.getAvailableTeamsFor(member);
	}
	
	public ArrayList<String> getTeamsFor(String member) {
		return DataBaseManager.getTeamsFor(member);
	}
	
	public void addAffiliation(String member, String team) {
		DataBaseManager.addAffiliation(member, team);
	}
	
	public void removeAffiliation(String member, String team) {
		DataBaseManager.removeAffiliation(member, team);
	}
	
	public ArrayList<String> getAvailableMembersFor(String team) {
		return DataBaseManager.getAvailableMembersFor(team);
	}

	public ArrayList<String> getMembersFor(String team) {
		return DataBaseManager.getMembersFor(team);
	}
	
	public boolean inUse(int index) {
		return DataBaseManager.inUse(index);
	}

	public void setInUse(int index, boolean b) {
		DataBaseManager.setInUse(index, b);
	}
}