package control;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import model.ActionItem;

/*
 * DataSync Class
 * Copyright: Copyright © 2015
 *
 * @author Krishna Sai
 * @version 1
 */
public class DataSync implements Runnable{
	
	private static final String localJdbcUrl = "jdbc:mysql://localhost:3306/", centralJdbcUrl = "jdbc:mysql://10.10.10.125:3306/", dbName = "della10", dbUserName = "root", dbPwd = "root";		//DB DETAILS
	private static final String credentialsTable = dbName+".Users";						//TABLE SCHEMA
	private static final String actionItemsTable = dbName+".ActionItems";
	private static final String membersTable = dbName+".Members";
	private static final String teamsTable = dbName+".Teams";
	private static final String memberTeamTable = dbName+".MemberTeam";	

	private static Connection conn1;
	private static Connection conn2;
	private static ResultSet rs1;
	private static ResultSet rs2;
	private static Statement stmt1;
	private static Statement stmt2;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 
	 * Synchronizes Local database with the database stored on local server
	 * deletes all tables and adds the fresh set
	 */
	@Override
	public void run() {
		String query;
		try{ 
			Class.forName("com.mysql.jdbc.Driver");   // this will load the class Driver
			conn1 = DriverManager.getConnection(centralJdbcUrl, dbUserName, dbPwd);
			conn2 = DriverManager.getConnection(localJdbcUrl, dbUserName, dbPwd);
			
			stmt1 = conn1.createStatement();
			query = "show databases like '" + dbName +"'";
			rs1 = stmt1.executeQuery(query);
			if (!rs1.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt1.executeUpdate(query)==0){
					return;
				}
			}

			stmt2 = conn2.createStatement();
			query = "show databases like '" + dbName +"'";
			rs2 = stmt2.executeQuery(query);
			if (!rs2.next()){
				System.out.println("Creating DB");
				query= "CREATE DATABASE "+dbName;
				if (stmt2.executeUpdate(query)==0){
					return;
				}
			}
			
			conn1 = DriverManager.getConnection(centralJdbcUrl+dbName, dbUserName, dbPwd);
			conn2 = DriverManager.getConnection(localJdbcUrl+dbName, dbUserName, dbPwd);
			
			stmt1 = conn1.createStatement();
			DatabaseMetaData meta = conn1.getMetaData();
			rs1 = meta.getTables(null, null, credentialsTable, null);			
			if (!rs1.next()){ //Checking if table exists, created otherwise				
				query="create table " + credentialsTable +"(name varchar(100), username varchar(20) PRIMARY KEY, password varchar(20), credentials varchar(20))";
				if(stmt1.executeUpdate(query)!=0){
					return;
				}
			}
			rs1 = meta.getTables(null, null, actionItemsTable, null);			
			if (!rs1.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + actionItemsTable +"(Serial_Number integer Auto_Increment PRIMARY KEY, ActionItem_Name varchar(100), Description varchar(1000), Resolution varchar(1000), Creation_Date DATE, Due_Date DATE, Status varchar(10), Assigned_to_Member varchar(100), Assigned_to_Team varchar(100), In_Use boolean default false, FOREIGN KEY (Assigned_to_Member) REFERENCES Members(Member_Name),  FOREIGN KEY (Assigned_to_Team) REFERENCES teams(Team_Name))";  
				if(stmt1.executeUpdate(query)!=0){
					return;
				}
			}
			rs1 = meta.getTables(null, null, membersTable, null);			
			if (!rs1.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + membersTable +"(Member_Name varchar(100) PRIMARY KEY)";
				if(stmt1.executeUpdate(query)!=0){
					return;
				}
			}
			rs1 = meta.getTables(null, null, teamsTable, null);			
			if (!rs1.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + teamsTable +"(Team_Name varchar(100) PRIMARY KEY)";
				if(stmt1.executeUpdate(query)!=0){
					return;
				}
			}
			rs1 = meta.getTables(null, null, memberTeamTable, null);			
			if (!rs1.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + memberTeamTable +"(Member_Name varchar(100), Team_Name varchar(100), FOREIGN KEY (Member_Name) REFERENCES Members(Member_Name),  FOREIGN KEY (Team_Name) REFERENCES teams(Team_Name))";
				if(stmt1.executeUpdate(query)!=0){
					return;
				}
			}
			
			stmt2 = conn2.createStatement();
			meta = conn2.getMetaData();
			rs2 = meta.getTables(null, null, credentialsTable, null);			
			if (!rs2.next()){ //Checking if table exists, created otherwise				
				query="create table " + credentialsTable +"(name varchar(100), username varchar(20) PRIMARY KEY, password varchar(20), credentials varchar(20))";
				if(stmt1.executeUpdate(query)!=0){
					return;
				}
			}
			query = "DELETE FROM "+credentialsTable;
			stmt2.executeUpdate(query);
			
			rs2 = meta.getTables(null, null, memberTeamTable, null);			
			if (!rs2.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + memberTeamTable +"(Member_Name varchar(100), Team_Name varchar(100), FOREIGN KEY (Member_Name) REFERENCES Members(Member_Name),  FOREIGN KEY (Team_Name) REFERENCES teams(Team_Name))";
				if(stmt2.executeUpdate(query)!=0){
					return;
				}
			}
			query = "DELETE FROM "+memberTeamTable;
			stmt2.executeUpdate(query);
			
			rs2 = meta.getTables(null, null, actionItemsTable, null);			
			if (!rs2.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + actionItemsTable +"(Serial_Number integer Auto_Increment PRIMARY KEY, ActionItem_Name varchar(100), Description varchar(1000), Resolution varchar(1000), Creation_Date DATE, Due_Date DATE, Status varchar(10), Assigned_to_Member varchar(100), Assigned_to_Team varchar(100), In_Use boolean default false, FOREIGN KEY (Assigned_to_Member) REFERENCES Members(Member_Name),  FOREIGN KEY (Assigned_to_Team) REFERENCES teams(Team_Name))";  
				if(stmt2.executeUpdate(query)!=0){
					return;
				}
			}
			query = "DELETE FROM "+actionItemsTable;
			stmt2.executeUpdate(query);
			
			rs2 = meta.getTables(null, null, membersTable, null);			
			if (!rs2.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + membersTable +"(Member_Name varchar(100) PRIMARY KEY)";
				if(stmt2.executeUpdate(query)!=0){
					return;
				}
			}
			query = "DELETE FROM "+membersTable;
			stmt2.executeUpdate(query);
			
			rs2 = meta.getTables(null, null, teamsTable, null);			
			if (!rs2.next()){ //Checking if table exists, created otherwise				
				query="CREATE TABLE " + teamsTable +"(Team_Name varchar(100) PRIMARY KEY)";
				if(stmt2.executeUpdate(query)!=0){
					return;
				}
			}
			query = "DELETE FROM "+teamsTable;
			stmt2.executeUpdate(query);
			
			query =  "select * from "+ credentialsTable;
			rs1 = stmt1.executeQuery(query);
			while(rs1.next()) {
				String name = rs1.getString("name");
				String uname = rs1.getString("username");
				String pass = rs1.getString("password");
				String status = rs1.getString("credentials");
				query = "insert into "+credentialsTable+" values('"+name+"','"+uname+"','"+pass+"','"+status+"')";
				stmt2.executeUpdate(query);
			}
			
			query =  "select * from "+ membersTable;
			rs1 = stmt1.executeQuery(query);
			while(rs1.next()) {
				String member = rs1.getString("Member_Name");
				query =  "INSERT INTO "+membersTable+" VALUES('"+member+"')";
				stmt2.executeUpdate(query);
			}
			
			query =  "select * from "+ teamsTable;
			rs1 = stmt1.executeQuery(query);
			while(rs1.next()) {
				String team = rs1.getString("Team_Name");
				query =  "INSERT INTO "+teamsTable+" VALUES('"+team+"')";
				stmt2.executeUpdate(query);
			}
			
			query =  "select * from "+ memberTeamTable;
			rs1 = stmt1.executeQuery(query);
			while(rs1.next()) {
				String member = rs1.getString("Member_Name");
				String team = rs1.getString("Team_Name");
				query =  "INSERT INTO "+memberTeamTable+" VALUES('"+member+"','"+team+"')";
				stmt2.executeUpdate(query);
			}
			
			query =  "select * from "+ actionItemsTable;
			rs1 = stmt1.executeQuery(query);
			while(rs1.next()) {
				String name = rs1.getString("ActionItem_Name");
				String description = rs1.getString("Description");
				String resolution = rs1.getString("Resolution");
				String cDate = rs1.getString("Creation_Date");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date creationDate = new Date(sdf1.parse(cDate).getTime());
				String dDate = rs1.getString("Due_Date");
				java.sql.Date dueDate = new Date(sdf1.parse(dDate).getTime());
				String status = rs1.getString("Status");
				String atm = rs1.getString("Assigned_to_Member");
				String att = rs1.getString("Assigned_to_Team");
				ActionItem ai = new ActionItem(name, description, resolution, creationDate, dueDate, status, atm, att);
				query =  "INSERT INTO "+actionItemsTable+"(ActionItem_Name, Description, Resolution, Creation_Date, Due_Date, Status, Assigned_to_Member, Assigned_to_Team) VALUES('"+ai.getActionItemName()+"','"+ai.getDescription()+"','"+ai.getResolution()+"','"+ai.getCreatedDate()+"','"+ai.getDueDate()+"','"+ai.getStatus()+"','"+ai.getAssignedMember()+"','"+ai.getAssignedTeam()+"')";
				stmt2.executeUpdate(query);
			}
			System.out.println("SYNC Completed");
		}
		catch (Exception e) {
			System.out.println("SYNC Failed");
			e.printStackTrace();
		}
		close();
		return;
	}
	
	//Closes all database connections,returns and displays login result in appropriate color.
	private static void close(){
		try {
			if (stmt1!=null) stmt1.close();
			if (conn1!=null) conn1.close();
			if (rs1!=null) rs1.close();
			
			if (stmt2!=null) stmt2.close();
			if (conn2!=null) conn2.close();
			if (rs2!=null) rs2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
