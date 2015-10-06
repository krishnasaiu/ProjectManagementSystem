package persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.ActionItem;

/*
 * DataBaseManager Class
 * Connectivity with the database is controller here
 * Copyright: Copyright © 2015
 *
 * @author Kranthi
 * @version 1
 */
public class DataBaseManager {
	private static final String centralJdbcUrl = "jdbc:mysql://10.10.10.125:3306/", localJdbcUrl = "jdbc:mysql://localhost:3306/", dbName = "della10", dbUserName = "root", dbPwd = "root";		//DB DETAILS
	private static final String credentialsTable = "Users", userNameColumn = "username", pwdColumn="password";						//TABLE SCHEMA
	private static final String defaultUserName = "krishna", defaultPwd = "krishna";
	private static final String actionItemsTable = "ActionItems";
	private static final String membersTable = "Members";
	private static final String teamsTable = "Teams";
	private static final String memberTeamTable = "MemberTeam";	

	private static final String[] inclusionFactor = {"", " Status='Open'", " Status='Closed'"};
	private static final String[] sortOrder = {"" , " Creation_Date", " Due_Date" , " Assigned_to_Member" , " Assigned_to_Team" };
	private static final String[] sortDirection = {" LENGTH(ActionItem_Name) ASC", " LENGTH(ActionItem_Name) DESC"};

	private static Connection conn;
	private static ResultSet rs;
	private static Statement stmt;	

	public static String connection(String UName,String Password){
		if (UName == null || UName.length()==0){
			close();
			return "User name can't be empty";
		}
		else if (Password == null || Password.length()==0){
			close();
			return "Password can't be empty";
		}
		String query;
		try{ 
			conn = DriverManager.getConnection(localJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return "Can't create database";
				}
			}
			conn = DriverManager.getConnection(localJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, credentialsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="create table " + credentialsTable +"(name varchar(100), username varchar(20) PRIMARY KEY, password varchar(20), credentials varchar(20))";
				if(stmt.executeUpdate(query)!=0){
					close();
					return "Can't create table";
				}
				query = "insert into "+credentialsTable+" values('user','"+defaultUserName+"','"+defaultPwd+"','employee')";
				if(stmt.executeUpdate(query)==0){
					close();
					return "Default account creation Failure";
				}
			}
			query =  "select * from "+ credentialsTable+  " where " + userNameColumn + " = '" + UName + "' and " + pwdColumn + " = '"+ Password + "'";
			rs = stmt.executeQuery(query);
			if (rs.next()){
				String reply = rs.getString("credentials");
				close();
				return reply ;
			}
			else{
				close();
				return "Invalid credentials";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return "Can't access MySQL database";
	}

	public static boolean writeActionItem(ActionItem ai){
		if (ai == null){
			close();
			return false;
		}
		String query;
		try{ 
			conn = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return false;
				}
			}
			conn = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, actionItemsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + actionItemsTable +"(Serial_Number integer Auto_Increment PRIMARY KEY, ActionItem_Name varchar(100), Description varchar(1000), Resolution varchar(1000), Creation_Date DATE, Due_Date DATE, Status varchar(10), Assigned_to_Member varchar(100), Assigned_to_Team varchar(100), In_Use boolean default false, FOREIGN KEY (Assigned_to_Member) REFERENCES Members(Member_Name),  FOREIGN KEY (Assigned_to_Team) REFERENCES teams(Team_Name))";  
				if(stmt.executeUpdate(query)!=0){
					close();
					return false;
				}
			}
			query =  "INSERT INTO "+actionItemsTable+"(ActionItem_Name, Description, Resolution, Creation_Date, Due_Date, Status, Assigned_to_Member, Assigned_to_Team) VALUES('"+ai.getActionItemName()+"','"+ai.getDescription()+"','"+ai.getResolution()+"','"+ai.getCreatedDate()+"','"+ai.getDueDate()+"','"+ai.getStatus()+"','"+ai.getAssignedMember()+"','"+ai.getAssignedTeam()+"')";
			if(stmt.executeUpdate(query)==0){
				close();
				return false;
			}
			else{
				close();
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return false;
	}

	public static boolean writeActionItem(int index, ActionItem ai) {
		if (ai == null){
			close();
			return false;
		}
		String query;
		try{ 
			conn = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return false;
				}
			}
			conn = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, actionItemsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + actionItemsTable +"(Serial_Number integer Auto_Increment PRIMARY KEY, ActionItem_Name varchar(100), Description varchar(1000), Resolution varchar(1000), Creation_Date DATE, Due_Date DATE, Status varchar(10), Assigned_to_Member varchar(100), Assigned_to_Team varchar(100), In_Use boolean default false, FOREIGN KEY (Assigned_to_Member) REFERENCES Members(Member_Name),  FOREIGN KEY (Assigned_to_Team) REFERENCES teams(Team_Name))";  
				if(stmt.executeUpdate(query)!=0){
					close();
					return false;
				}
			}
			query =  "UPDATE "+actionItemsTable+" SET ActionItem_Name='"+ai.getActionItemName()+"', Description='"+ai.getDescription()+"', Resolution='"+ai.getResolution()+"', Creation_Date='"+ai.getCreatedDate()+"', Due_Date='"+ai.getDueDate()+"', Status='"+ai.getStatus()+"', Assigned_to_Member='"+ai.getAssignedMember()+"', Assigned_to_Team='"+ai.getAssignedTeam()+"' WHERE Serial_Number="+index;
			System.out.println(query);
			if(stmt.executeUpdate(query)==0){
				close();
				return false;
			}
			else{
				close();
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return false;
	}


	public static boolean deleteActionItem(int index) {
		String query;
		try{ 
			conn = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return false;
				}
			}
			conn = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, actionItemsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + actionItemsTable +"(Serial_Number integer Auto_Increment PRIMARY KEY, ActionItem_Name varchar(100), Description varchar(1000), Resolution varchar(1000), Creation_Date DATE, Due_Date DATE, Status varchar(10), Assigned_to_Member varchar(100), Assigned_to_Team varchar(100), In_Use boolean default false, FOREIGN KEY (Assigned_to_Member) REFERENCES Members(Member_Name),  FOREIGN KEY (Assigned_to_Team) REFERENCES teams(Team_Name));  ";
				if(stmt.executeUpdate(query)!=0){
					close();
					return false;
				}
			}
			query =  "DELETE FROM "+actionItemsTable+" WHERE Serial_Number="+index;
			System.out.println(query);
			if(stmt.executeUpdate(query)==0){
				close();
				return false;
			}
			else{
				close();
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return false;
	}

	public static ArrayList<ActionItem> getActionItems(int direction, int firstOrder, int secondOrder, int inclusion){
		String query;
		ArrayList<ActionItem> actionItems = new ArrayList<ActionItem>();
		try{ 
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(localJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0)
					return actionItems;          		
			}
			conn = DriverManager.getConnection(localJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, actionItemsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + actionItemsTable +"(Serial_Number integer Auto_Increment PRIMARY KEY, ActionItem_Name varchar(100), Description varchar(1000), Resolution varchar(1000), Creation_Date DATE, Due_Date DATE, Status varchar(10), Assigned_to_Member varchar(100), Assigned_to_Team varchar(100), In_Use boolean default false, FOREIGN KEY (Assigned_to_Member) REFERENCES Members(Member_Name),  FOREIGN KEY (Assigned_to_Team) REFERENCES teams(Team_Name));  ";
				if(stmt.executeUpdate(query)!=0)
					return actionItems;
			}
			query =  "SELECT * FROM "+actionItemsTable;

			if(inclusion > 0)
				query += " WHERE "+inclusionFactor[inclusion];

			query += " ORDER BY";
			
			if(firstOrder > 0)
				query += sortOrder[firstOrder];
			if(secondOrder > 0){
				if(firstOrder>0)
					query += ","+sortOrder[secondOrder];
				else
					query += sortOrder[secondOrder];
			}
			
			if(firstOrder>0 || secondOrder>0)
				query += ","+sortDirection[direction];
			else
				query += sortDirection[direction];
			
			System.out.println(query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				String name = rs.getString("ActionItem_Name");
				String description = rs.getString("Description");
				String resolution = rs.getString("Resolution");
				String cDate = rs.getString("Creation_Date");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date creationDate = new Date(sdf1.parse(cDate).getTime());
				String dDate = rs.getString("Due_Date");
				java.sql.Date dueDate = new Date(sdf1.parse(dDate).getTime());
				String status = rs.getString("Status");
				String atm = rs.getString("Assigned_to_Member");
				String att = rs.getString("Assigned_to_Team");
				actionItems.add(new ActionItem(name, description, resolution, creationDate, dueDate, status, atm, att));
			}
			return actionItems;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return actionItems;
	}

	public static ActionItem getSelectedActionItem(String aiName){
		String query;
		ActionItem actionItem = new ActionItem();
		try{ 
			conn = DriverManager.getConnection(localJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0)
					return actionItem;          		
			}
			conn = DriverManager.getConnection(localJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, actionItemsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + actionItemsTable +"(Serial_Number integer Auto_Increment PRIMARY KEY, ActionItem_Name varchar(100), Description varchar(1000), Resolution varchar(1000), Creation_Date DATE, Due_Date DATE, Status varchar(10), Assigned_to_Member varchar(100), Assigned_to_Team varchar(100), In_Use boolean default false, FOREIGN KEY (Assigned_to_Member) REFERENCES Members(Member_Name),  FOREIGN KEY (Assigned_to_Team) REFERENCES teams(Team_Name));  ";
				if(stmt.executeUpdate(query)!=0)
					return actionItem;
			}
			query =  "SELECT * FROM "+actionItemsTable+" WHERE ActionItem_Name = '"+aiName+"'";

			rs = stmt.executeQuery(query);
			while(rs.next()) {
				String name = rs.getString("ActionItem_Name");
				String description = rs.getString("Description");
				String resolution = rs.getString("Resolution");
				String cDate = rs.getString("Creation_Date");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date creationDate = new Date(sdf1.parse(cDate).getTime());
				String dDate = rs.getString("Due_Date");
				java.sql.Date dueDate = new Date(sdf1.parse(dDate).getTime());
				String status = rs.getString("Status");
				String atm = rs.getString("Assigned_to_Member");
				String att = rs.getString("Assigned_to_Team");
				actionItem = new ActionItem(name, description, resolution, creationDate, dueDate, status, atm, att);
			}
			return actionItem;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return actionItem;
	}

	public static int getSerialNumber(ActionItem ai) {
		String query;
		int index = -1;
		try{ 
			conn = DriverManager.getConnection(localJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0)
					return index;          		
			}
			conn = DriverManager.getConnection(localJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, actionItemsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + actionItemsTable +"(Serial_Number integer Auto_Increment PRIMARY KEY, ActionItem_Name varchar(100), Description varchar(1000), Resolution varchar(1000), Creation_Date DATE, Due_Date DATE, Status varchar(10), Assigned_to_Member varchar(100), Assigned_to_Team varchar(100), In_Use boolean default false, FOREIGN KEY (Assigned_to_Member) REFERENCES Members(Member_Name),  FOREIGN KEY (Assigned_to_Team) REFERENCES teams(Team_Name));  ";
				if(stmt.executeUpdate(query)!=0)
					return index;
			}
			query =  "SELECT Serial_Number FROM "+actionItemsTable+" WHERE ActionItem_Name = '"+ai.getActionItemName()+"'";

			rs = stmt.executeQuery(query);
			while(rs.next()) {
				index = rs.getInt("Serial_Number");
			}
			return index;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return index;
	}

	public static ArrayList<String> getMembers(){
		String query;
		ArrayList<String> members = new ArrayList<String>();
		try{ 
			conn = DriverManager.getConnection(localJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0)
					return members;          		
			}
			conn = DriverManager.getConnection(localJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, membersTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + membersTable +"(Member_Name varchar(100) PRIMARY KEY)";
				if(stmt.executeUpdate(query)!=0)
					return members;
			}
			query =  "SELECT Member_Name FROM "+membersTable;

			rs = stmt.executeQuery(query);
			while(rs.next()) {
				members.add(rs.getString("Member_Name"));
			}
			return members;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return members;
	}

	public static boolean saveNewMember(String member) {
		String query;
		if(member.length() == 0 || member == null){
			close();
			return false;
		}
		try{ 
			conn = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0)
					return false;          		
			}
			conn = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, membersTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + membersTable +"(Member_Name varchar(100) PRIMARY KEY)";
				if(stmt.executeUpdate(query)!=0)
					return false;
			}
			query =  "INSERT INTO "+membersTable+" VALUES('"+member+"')";
			System.out.println(query);

			if(stmt.executeUpdate(query)==0){
				close();
				return false;
			}
			else{
				close();
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return false;
	}

	public static boolean removeMember(String member) {
		String query;
		if(member.length() == 0 || member == null){
			close();
			System.out.println("1");
			return false;
		}
		try{ 
			conn = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					System.out.println("2");
					return false;          		
				}
			}
			conn = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, membersTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + membersTable +"(Member_Name varchar(100) PRIMARY KEY)";
				if(stmt.executeUpdate(query)!=0){
					System.out.println("");
					System.out.println("3");
					return false;
				}
			}
			rs = meta.getTables(null, null, teamsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + teamsTable +"(Team_Name varchar(100) PRIMARY KEY)";
				if(stmt.executeUpdate(query)!=0){
					close();
					System.out.println("4");
					return false;
				}
			}
			rs = meta.getTables(null, null, memberTeamTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + memberTeamTable +"(Member_Name varchar(100), Team_Name varchar(100), FOREIGN KEY (Member_Name) REFERENCES Members(Member_Name),  FOREIGN KEY (Team_Name) REFERENCES teams(Team_Name))";
				if(stmt.executeUpdate(query)!=0){
					close();
					System.out.println("5");
					return false;
				}
			}

			query =  "DELETE FROM "+memberTeamTable+" WHERE Member_Name='"+member+"'";
			System.out.println(query);

			stmt.executeUpdate(query);
			query =  "DELETE FROM "+membersTable+" WHERE Member_Name='"+member+"'";
			System.out.println(query);

			if(stmt.executeUpdate(query)==0){
				close();
				System.out.println("7");
				return false;
			}
			else{
				close();
				System.out.println("8");
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		System.out.println("9");
		return false;
	}

	public static ArrayList<String> getTeams(){
		String query;
		ArrayList<String> teams = new ArrayList<String>();
		try{ 
			conn = DriverManager.getConnection(localJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0)
					return teams;          		
			}
			conn = DriverManager.getConnection(localJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, teamsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + teamsTable +"(Team_Name varchar(100) PRIMARY KEY)";
				if(stmt.executeUpdate(query)!=0)
					return teams;
			}
			query =  "SELECT Team_Name FROM "+teamsTable;

			rs = stmt.executeQuery(query);
			while(rs.next()) {
				teams.add(rs.getString("Team_Name"));
			}
			return teams;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return teams;
	}

	public static boolean saveNewTeam(String team) {
		String query;
		if(team.length() == 0 || team == null){
			close();
			return false;
		}
		try{ 
			conn = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0)
					return false;          		
			}
			conn = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, teamsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + teamsTable +"(Team_Name varchar(100) PRIMARY KEY)";
				if(stmt.executeUpdate(query)!=0)
					return false;
			}
			query =  "INSERT INTO "+teamsTable+" VALUES('"+team+"')";
			System.out.println(query);

			if(stmt.executeUpdate(query)==0){
				close();
				return false;
			}
			else{
				close();
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return false;
	}

	public static boolean removeTeam(String team) {
		String query;
		if(team.length() == 0 || team == null){
			close();
			return false;
		}
		try{ 
			conn = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return false;          		
				}
			}
			conn = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, membersTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + membersTable +"(Member_Name varchar(100) PRIMARY KEY)";
				if(stmt.executeUpdate(query)!=0){
					System.out.println("");
					return false;
				}
			}
			rs = meta.getTables(null, null, teamsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + teamsTable +"(Team_Name varchar(100) PRIMARY KEY)";
				if(stmt.executeUpdate(query)!=0){
					close();
					return false;
				}
			}
			rs = meta.getTables(null, null, memberTeamTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + memberTeamTable +"(Member_Name varchar(100), Team_Name varchar(100), FOREIGN KEY (Member_Name) REFERENCES Members(Member_Name),  FOREIGN KEY (Team_Name) REFERENCES teams(Team_Name))";
				if(stmt.executeUpdate(query)!=0){
					close();
					return false;
				}
			}

			query =  "DELETE FROM "+memberTeamTable+" WHERE Team_Name='"+team+"'";
			System.out.println(query);

			stmt.executeUpdate(query);
			query =  "DELETE FROM "+teamsTable+" WHERE Team_Name='"+team+"'";
			System.out.println(query);

			if(stmt.executeUpdate(query)==0){
				close();
				return false;
			}
			else{
				close();
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return false;
	}

	public static ArrayList<String> getAvailableTeamsFor(String member) {
		ArrayList<String> teams = new ArrayList<String>();
		String query;
		if(member.length() == 0 || member == null){
			close();
			return teams;
		}
		try{ 
			conn = DriverManager.getConnection(localJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return teams;          		
				}
			}
			conn = DriverManager.getConnection(localJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, teamsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + teamsTable +"(Team_Name varchar(100) PRIMARY KEY)";
				if(stmt.executeUpdate(query)!=0){
					close();
					return teams;
				}
			}
			rs = meta.getTables(null, null, memberTeamTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + memberTeamTable +"(Member_Name varchar(100), Team_Name varchar(100), FOREIGN KEY (Member_Name) REFERENCES Members(Member_Name),  FOREIGN KEY (Team_Name) REFERENCES teams(Team_Name))";
				if(stmt.executeUpdate(query)!=0){
					close();
					return teams;
				}
			}

			query =  "select Team_Name from "+teamsTable+" where Team_Name not in (select team_name from "+memberTeamTable+" where Member_Name='"+member+"')";
			System.out.println(query);

			rs = stmt.executeQuery(query);
			while(rs.next()) {
				teams.add(rs.getString("Team_Name"));
			}
			return teams;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return teams;
	}

	public static ArrayList<String> getTeamsFor(String member) {

		ArrayList<String> teams = new ArrayList<String>();
		String query;
		if(member.length() == 0 || member == null){
			close();
			return teams;
		}
		try{ 
			conn = DriverManager.getConnection(localJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return teams;          		
				}
			}
			conn = DriverManager.getConnection(localJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, memberTeamTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + memberTeamTable +"(Member_Name varchar(100), Team_Name varchar(100), FOREIGN KEY (Member_Name) REFERENCES Members(Member_Name),  FOREIGN KEY (Team_Name) REFERENCES teams(Team_Name))";
				if(stmt.executeUpdate(query)!=0){
					close();
					return teams;
				}
			}

			query =  "select team_name from "+memberTeamTable+" where Member_Name='"+member+"'";
			System.out.println(query);

			rs = stmt.executeQuery(query);
			while(rs.next()) {
				teams.add(rs.getString("Team_Name"));
			}
			return teams;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return teams;
	}

	public static boolean addAffiliation(String member, String team) {
		String query;
		if(member.length() == 0 || member == null){
			close();
			return false;
		}
		try{ 
			conn = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return false;          		
				}
			}
			conn = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, memberTeamTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + memberTeamTable +"(Member_Name varchar(100), Team_Name varchar(100), FOREIGN KEY (Member_Name) REFERENCES Members(Member_Name),  FOREIGN KEY (Team_Name) REFERENCES teams(Team_Name))";
				if(stmt.executeUpdate(query)!=0){
					close();
					return false;
				}
			}

			query =  "insert into "+memberTeamTable+" values('"+member+"','"+team+"')";
			System.out.println(query);

			if(stmt.executeUpdate(query)==0){
				close();
				return false;
			}
			else{
				close();
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return false;
	}

	public static boolean removeAffiliation(String member, String team) {
		String query;
		if(member.length() == 0 || member == null){
			close();
			System.out.println("1");
			return false;
		}
		try{ 
			conn = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					System.out.println("2");
					return false;          		
				}
			}
			conn = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, memberTeamTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + memberTeamTable +"(Member_Name varchar(100), Team_Name varchar(100), FOREIGN KEY (Member_Name) REFERENCES Members(Member_Name),  FOREIGN KEY (Team_Name) REFERENCES teams(Team_Name))";
				if(stmt.executeUpdate(query)!=0){
					close();
					System.out.println("5");
					return false;
				}
			}

			query =  "DELETE FROM "+memberTeamTable+" WHERE Member_Name='"+member+"' AND Team_Name='"+team+"'";
			System.out.println(query);
			if(stmt.executeUpdate(query)==0){
				close();
				return false;
			}
			else{
				close();
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return false;
	}

	public static ArrayList<String> getAvailableMembersFor(String team) {
		ArrayList<String> members = new ArrayList<String>();
		String query;
		if(team.length() == 0 || team == null){
			close();
			return members;
		}
		try{ 
			conn = DriverManager.getConnection(localJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return members;          		
				}
			}
			conn = DriverManager.getConnection(localJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, membersTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + membersTable +"(Member_Name varchar(100) PRIMARY KEY)";
				if(stmt.executeUpdate(query)!=0){
					System.out.println("");
					return members;
				}
			}
			rs = meta.getTables(null, null, memberTeamTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + memberTeamTable +"(Member_Name varchar(100), Team_Name varchar(100), FOREIGN KEY (Member_Name) REFERENCES Members(Member_Name),  FOREIGN KEY (Team_Name) REFERENCES teams(Team_Name))";
				if(stmt.executeUpdate(query)!=0){
					close();
					return members;
				}
			}

			query =  "select Member_Name from "+membersTable+" where Member_Name not in (select Member_Name from "+memberTeamTable+" where Team_Name='"+team+"')";
			System.out.println(query);

			rs = stmt.executeQuery(query);
			while(rs.next()) {
				members.add(rs.getString("Member_Name"));
			}
			return members;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return members;
	}

	public static ArrayList<String> getMembersFor(String team) {
		ArrayList<String> members = new ArrayList<String>();
		String query;
		if(team.length() == 0 || team == null){
			close();
			return members;
		}
		try{ 
			conn = DriverManager.getConnection(localJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return members;          		
				}
			}
			conn = DriverManager.getConnection(localJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, memberTeamTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + memberTeamTable +"(Member_Name varchar(100), Team_Name varchar(100), FOREIGN KEY (Member_Name) REFERENCES Members(Member_Name),  FOREIGN KEY (Team_Name) REFERENCES teams(Team_Name))";
				if(stmt.executeUpdate(query)!=0){
					close();
					return members;
				}
			}

			query =  "select Member_Name from "+memberTeamTable+" where Team_Name='"+team+"'";
			System.out.println(query);

			rs = stmt.executeQuery(query);
			while(rs.next()) {
				members.add(rs.getString("Member_Name"));
			}
			return members;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		close();
		return members;
	}

	//Closes all database connections,returns and displays login result in appropriate color.
	private static void close(){  	
		try {
			if (stmt!=null) stmt.close();
			if (conn!=null) conn.close();
			if (rs!=null) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean inUse(int index) {
		String query;
		try{ 
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return true;          		
				}
			}
			conn = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, actionItemsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + actionItemsTable +"(Serial_Number integer Auto_Increment PRIMARY KEY, ActionItem_Name varchar(100), Description varchar(1000), Resolution varchar(1000), Creation_Date DATE, Due_Date DATE, Status varchar(10), Assigned_to_Member varchar(100), Assigned_to_Team varchar(100), In_Use boolean default false, FOREIGN KEY (Assigned_to_Member) REFERENCES Members(Member_Name),  FOREIGN KEY (Assigned_to_Team) REFERENCES teams(Team_Name));  ";
				if(stmt.executeUpdate(query)!=0){
					close();
					return true;
				}
			}
			query =  "SELECT * FROM "+actionItemsTable+" where Serial_Number="+index;

			rs = stmt.executeQuery(query);
			if(rs.next())
				return rs.getBoolean("In_Use");
			else
				return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean setInUse(int index, boolean b) {
		String query;
		try{ 
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);            
			stmt = conn.createStatement();
			query = "show databases like '" + dbName +"'";
			rs = stmt.executeQuery(query);
			if (!rs.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt.executeUpdate(query)==0){
					close();
					return false;          		
				}
			}
			conn = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			stmt = conn.createStatement();
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, actionItemsTable, null);			
			if (!rs.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + actionItemsTable +"(Serial_Number integer Auto_Increment PRIMARY KEY, ActionItem_Name varchar(100), Description varchar(1000), Resolution varchar(1000), Creation_Date DATE, Due_Date DATE, Status varchar(10), Assigned_to_Member varchar(100), Assigned_to_Team varchar(100), In_Use boolean default false, FOREIGN KEY (Assigned_to_Member) REFERENCES Members(Member_Name),  FOREIGN KEY (Assigned_to_Team) REFERENCES teams(Team_Name));  ";
				if(stmt.executeUpdate(query)!=0){
					close();
					return false;
				}
			}
			query =  "update "+actionItemsTable+" set In_Use="+false+" where Serial_Number="+index;

			rs = stmt.executeQuery(query);
			if(rs.next())
				return rs.getBoolean("In_Use");
			else
				return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}