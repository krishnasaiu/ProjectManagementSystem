package model;

import java.sql.Date;
import java.util.Comparator;

/**
 * <p>
 * Title: ActionItem
 * </p>
 *
 * <p>
 * Description: An entity to hold details about a particular action item
 * </p>
 *
 * <p>
 * Copyright: Copyright © 2015
 * </p>
 *
 * @author Krishna Sai, Kranthi, Harshini
 * @version 1.10
 */
public class ActionItem {

	//---------------------------------------------------------------------------------------------------------------------
	// Attributes

	private String actionItemName;
	private String description;
	private String resolution;
	private String status;
	private Date dueDate;
	private Date createdDate;
	private String assignedMember;	// Added for Della05
	private String assignedTeam;	// Added for Della06
	//---------------------------------------------------------------------------------------------------------------------

	/**
	 * The ActionItem class constructors.
	 *
	 */
	public ActionItem() {
		actionItemName = description = resolution = status = assignedMember = assignedTeam = ""; // Updated for Della05 and Della09
		dueDate = createdDate = null;
	}

	public ActionItem(String name) {
		actionItemName = name;
		description = resolution = status = assignedMember = assignedTeam = ""; // Updated for Della05 and Della09
		dueDate = createdDate = null;
	}

	public ActionItem(String ai, String desc, String res, Date cDate, Date dDate, String stat, String memb, String team) { // Updated for Della05 and Della09

		actionItemName = ai;
		description = desc;
		resolution = res;
		status = stat;
		createdDate = cDate;
		dueDate = dDate;
		assignedMember = memb;	// Added for Della05
		assignedTeam = team;	// Added for Della09
	}

	@Override
	public String toString(){
		return actionItemName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionItem other = (ActionItem) obj;
		if (actionItemName == null) {
			if (other.actionItemName != null)
				return false;
		} else if (!actionItemName.equals(other.actionItemName))
			return false;
		return true;
	}
	
	public static final Comparator<ActionItem> largeToSmall = new Comparator<ActionItem>() {
		public int compare(ActionItem ai1, ActionItem ai2) {
			if(ai1.getActionItemName().length() > ai2.getActionItemName().length())
				return -1;
			else if(ai1.getActionItemName().length() == ai2.getActionItemName().length())
				return 0;
			else
				return 1;
		}
	};
	
	public static final Comparator<ActionItem> smallToLarge = new Comparator<ActionItem>() {
		public int compare(ActionItem ai1, ActionItem ai2) {
			if(ai1.getActionItemName().length() < ai2.getActionItemName().length())
				return -1;
			else if(ai1.getActionItemName().length() == ai2.getActionItemName().length())
				return 0;
			else
				return 1;
		}
	};

	// Just the usual getters and setters
	public String getActionItemName() { return actionItemName; }

	public String getDescription() { return description; }

	public String getResolution() { return resolution; }

	public String getStatus() { return status; }

	public Date getDueDate() { return dueDate; }

	public Date getCreatedDate() { return createdDate; }

	public void setActionItemName(String x) { actionItemName = x; }

	public void setDescription(String x) { description = x; }

	public void setResolution(String x) { resolution = x; }

	public void setStatus(String x) { status = x; }

	public void setDueDate(Date x) { dueDate = x; }

	public void setCreatedDate(Date x) { createdDate = x; }

	public String getAssignedMember() { return assignedMember; }	// Added for Della05

	public void setAssignedMember(String x) { assignedMember = x; } 	// Added for Della05  

	public String getAssignedTeam() { return assignedTeam; }	// Added for Della06

	public void setAssignedTeam(String x) { assignedTeam = x; } 	// Added for Della06  

}