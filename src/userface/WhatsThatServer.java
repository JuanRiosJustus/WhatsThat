package userface;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import architecture.Controller;
import architecture.Model;
import architecture.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import messaging.MessageType;
import network.Server;
import utlility.NetworkUtility;
import utlility.StringUtility;

public class WhatsThatServer extends Application {

	private BorderPane mainPane;
	private Scene mainScene;
	private TextArea mainArea;
	private TextField msgField;
	private TextField portField;
	
	private final ConcurrentHashMap<String, PrintWriter> map = new ConcurrentHashMap<>();
	private Controller controls;
	private final double heightMulti = .8;
	
    private final int programWidth = 1000;
    private final int programHeight = 520;
	
	private Server mainServer;
	private Thread mainThread;
	private boolean isRunning;

	@Override
	public void start(Stage arg0) throws Exception {
		initializeComponents(arg0);
		
	}
	private void initializeComponents(Stage mainStage) {
		mainStage.setWidth(programWidth);
		mainStage.setHeight(programHeight);
		mainStage.setTitle("WhatsThat Server");
		mainStage.setResizable(false);
		mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override public void handle(WindowEvent arg0) {
				Platform.exit();
				System.exit(0);
			}
		});
		
		mainPane = new BorderPane();
		mainScene = new Scene(mainPane, programWidth, programHeight);
		
		mainArea = initializeMainTextArea();
		controls = new Controller(new Model(true), new View(null, mainArea));
		mainArea.setEditable(false);
		mainPane.setTop(mainArea);
		
		FlowPane fp = initializeButtonComponents();
		mainPane.setCenter(fp);
		
		mainStage.setScene(mainScene);
		mainStage.show();
	}
	// TODO the buttons will not show because of this area.
	private TextArea initializeMainTextArea() {
		TextArea area = new TextArea(NetworkUtility.getHostAddresses());
		area.setMaxHeight(programHeight * heightMulti);
		area.setPrefHeight(programHeight * heightMulti);
		area.setMinHeight(programHeight * heightMulti);
		area.setPrefRowCount(5);
		return area;
	}
	
	private FlowPane initializeButtonComponents() {
		FlowPane fp = new FlowPane();
		// ----------------------------- components to send to all clients -----------------------/
		Label msgLabel = new Label("  Log: ");
		msgField = new TextField();
		msgField.setEditable(false);
		Button msgButton = new Button("Send");
		msgButton.setDisable(true);
		msgButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				if (NetworkUtility.isValidMessage(msgField.getText()) == false) { return; }
				announce(MessageType.constructServerMesaage(msgField.getText()));
				msgField.clear();
			}
		});
		
		// ------------------------- Configurations for the start button --------------- //
		Button startButton = new Button("Start Server");
		startButton.setDisable(true);
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				startServerAction();
				msgButton.setDisable(false);
				msgField.setEditable(true);
			}
		});
		// ---------------------- configurations for the stop button --------------- //
		Button stopButton = new Button("Stop Server");
		stopButton.setDisable(true);
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				stopServerAction();
			}
		});
		// --------------------- configurations for the check user button ------------ //
		Button checkUsersButton = new Button("Online Users");
		checkUsersButton.setDisable(true);
		checkUsersButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				checkUsersAction();
			}
		});
		// ------------------- configurations for the clear log button ------------- // 
		Button clearButton = new Button("Clear Log");
		clearButton.setDisable(true);
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				mainArea.clear();
			}
		});
		// -------------------- configurations for port info and  --------------------------- //
		Label portLabel = new Label("  Port: ");
		portField = new TextField(2757 + "");
		
		// ---------------- configurations for the finalize button -------------- //
		Button finalizeButton = new Button("Finalize Configuration");
		finalizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				// Check if the current port number is correct
				String portData = portField.getText();
				if (StringUtility.isNumeric(portData) == false) { 
					mainArea.appendText("Invalid Port Format. \n");
					return; 
				}
				int port = Integer.valueOf(portData).intValue();
				if (NetworkUtility.isValidPort(port) == false) { 
					mainArea.appendText("Invalid Port Number. \n");
					return; 
				}
				// the current port is valid, proceed to regular functionality.
				startButton.setDisable(false);
				//stopButton.setDisable(false);
				checkUsersButton.setDisable(false);
				clearButton.setDisable(false);
				portField.setEditable(false);
				finalizeButton.setDisable(true);
				msgField.setEditable(true);
				mainArea.appendText("Port has been finalized. \n");
			}
		});
		
		fp.getChildren().add(new Label("       "));
		fp.getChildren().add(startButton);
		fp.getChildren().add(stopButton);
		fp.getChildren().add(checkUsersButton);
		fp.getChildren().add(clearButton);
		fp.getChildren().add(portLabel);
		fp.getChildren().add(portField);
		fp.getChildren().add(finalizeButton);
		fp.getChildren().add(msgLabel);
		fp.getChildren().add(msgField);
		fp.getChildren().add(msgButton);
		return fp;
	}
	
	private void startServerAction() {
		if (isRunning == false) {
			isRunning = true;
			mainServer = new Server(map, mainArea, Integer.valueOf(portField.getText()));
			mainThread = new Thread(mainServer);
			mainThread.start();
			mainArea.appendText("Server has been started. \n");
		} else {
			mainArea.appendText("Server already running. \n");
		}
	}
	
	
	private void stopServerAction() {
		try {
			announce("Server: is stopping and all users will be disconnected");
			mainArea.appendText("Server stopping... \n");
			isRunning = false;
			Platform.exit();
			System.exit(0);
		} catch (Exception ex) {
			mainArea.appendText("Error stopping Server... \n");
		}
	}
	private void checkUsersAction() {
		mainArea.appendText("\n\n Current users : \n");
		Iterator<Entry<String, PrintWriter>> users = map.entrySet().iterator();
		while (users.hasNext()) {
			mainArea.appendText("[" + users.next().getKey() + "]");
		}
	}
	
	public void addUser(String givenUsername) {
		mainArea.appendText(givenUsername + " is connected.\n");
		mainArea.appendText("Telling users to add [" + givenUsername + "]\n");
		mainArea.positionCaret(mainArea.getLength());
		announce(givenUsername + MessageType.JustConnected.toString());
		announce(MessageType.ServerActionsCompleted.toString());
	}
	
	public void removeUser(String givenUsername) {
		map.remove(givenUsername);
		mainArea.appendText("[" + givenUsername + "] has been removed.\n");
		mainArea.appendText("Telling users to remove [" + givenUsername + "]\n");
		mainArea.positionCaret(mainArea.getLength());
		announce(givenUsername + MessageType.JustDisconnected.toString());
		announce(MessageType.ServerActionsCompleted.toString());
	}
	// tells everyone
	public void announce(String message) {
		Iterator<Entry<String, PrintWriter>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			try {
				Entry<String, PrintWriter> r = it.next();
				PrintWriter currentClient = r.getValue();
				currentClient.println(message);
				currentClient.flush();
			} catch (Exception ex) {
				// error
				mainArea.appendText("Error sending message to clients...\n");
			}
		}
	}
}
