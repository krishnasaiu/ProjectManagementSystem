package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import control.Controller;
import control.DataSync;
import control.InternetAvailibility;

/*
 * MainScreen Controller Class
 * Copyright: Copyright © 2015
 *
 * @author Kranthi
 * @version 1
 */
public class MainScreen implements Initializable{
	@FXML
	private Button consoleScreen;
	@FXML
	private Button actionItemsScreen;
	@FXML
	private Button memberScreen;
	@FXML
	private Button teamScreen;
	@FXML
	private Button quit;
	@FXML
	private Button sync;

	@FXML
	private AnchorPane anchCmdController;

	@FXML
	private TextField tfUsernameLogin;
	@FXML 
	private PasswordField pfPasswordLogin;
	@FXML
	private Button btLogin;
	@FXML
	private Label lbAlertLogin;

	@FXML
	private Label lbInternetAvailabilityMain;

	private Controller theController = null;
	private Timer timer;						
	private boolean loginSuccess = false;		// Checks if user is logged in or not
	private String userCred = null;				// Stores user name who logged in 
	private final int interval = 3;				// Interval after which check for internet and database sync is done
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		theController = Controller.getInstance();

		/*
		 * Checks for internet after a certain interval and modifies UI accordingly
		 */
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						InternetAvailibility ia = new InternetAvailibility();
						Thread t1 = new Thread(ia);
						t1.start();
						try {
							boolean flag = ia.get();
							if(!flag){
								lbInternetAvailabilityMain.setTextFill(Color.RED);
								lbInternetAvailabilityMain.setText("Not Available");
								actionItemsScreen.setDisable(true);
								memberScreen.setDisable(true);
								teamScreen.setDisable(true);
								try {
									if(loginSuccess)
										handleConsoleButton();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							} else{
								lbInternetAvailabilityMain.setTextFill(Color.GREEN);
								lbInternetAvailabilityMain.setText("Available");
								if(loginSuccess){
									actionItemsScreen.setDisable(false);
									if(userCred.equals("manager")){
										memberScreen.setDisable(false);
										teamScreen.setDisable(false);
									}
									else{
										memberScreen.setDisable(true);
										teamScreen.setDisable(true);
									}
								}
								DataSync ds = new DataSync();
								Thread t2 = new Thread(ds);
								t2.start();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}, 0*1000, interval*1000);
	}

	//	Handles login button on homescreen
	@FXML
	public void handleLoginButton() throws IOException{
		String username = tfUsernameLogin.getText();
		String password = pfPasswordLogin.getText();
		userCred = theController.isValidUser(username, password);
		if(userCred.equals("manager")){
			loginSuccess = true;
			consoleScreen.setDisable(false);
			actionItemsScreen.setDisable(false);
			memberScreen.setDisable(false);
			teamScreen.setDisable(false);
			handleConsoleButton();
		}
		else if(userCred.equals("employee")){
			loginSuccess = true;
			consoleScreen.setDisable(false);
			actionItemsScreen.setDisable(false);
			memberScreen.setDisable(true);
			teamScreen.setDisable(true);
			handleConsoleButton();
		}
		else{
			lbAlertLogin.setTextFill(Color.RED);
			lbAlertLogin.setText(userCred);
		}
	}

	//	handles console button on top pane
	@FXML
	private void handleConsoleButton() throws IOException{
		AnchorPane cmdPane = (AnchorPane) FXMLLoader.load(getClass().getResource("/guiFXML/ConsoleScreenFXML.fxml"));
		try {
			anchCmdController.getChildren().clear();
			anchCmdController.getChildren().add(cmdPane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	handles Action Items button on top pane
	@FXML
	private void handleActionItemsButton() throws IOException{
		AnchorPane cmdPane = (AnchorPane) FXMLLoader.load(getClass().getResource("/guiFXML/ActionItemsFXML.fxml"));
		try {
			anchCmdController.getChildren().clear();
			anchCmdController.getChildren().add(cmdPane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	handles Members button on top pane
	@FXML
	private void handleMemberScreenButton() throws IOException{
		AnchorPane cmdPane = (AnchorPane) FXMLLoader.load(getClass().getResource("/guiFXML/MemberScreenFXML.fxml"));
		try {
			anchCmdController.getChildren().clear();
			anchCmdController.getChildren().add(cmdPane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	handles Teams button on top pane
	@FXML
	private void handleTeamScreenButton() throws IOException{
		AnchorPane cmdPane = (AnchorPane) FXMLLoader.load(getClass().getResource("/guiFXML/TeamScreenFXML.fxml"));
		try {
			anchCmdController.getChildren().clear();
			anchCmdController.getChildren().add(cmdPane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//  handles Quit button on top pane
	@FXML
	private void handleQuitButton(){
		timer.cancel();
		System.exit(0);
	}

}
