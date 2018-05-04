package userface;

import java.util.ArrayList;

import architecture.Controller;
import architecture.Model;
import architecture.View;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.Client;
import utlility.NetworkUtility;

public class WhatsThatClient extends Application {

    private GraphicsContext context;
    private Scene scene;
    private VBox mainPane;
    private final double heightMulti = .8;
    private final double widthMulti = .5;
    private final int textColumns = 8;
    private final double componentWidthMulti = .31;
    private final double componentCanvasWidthMulti = .7;
    
    private TextField usernameField;
    private TextField addressField;
    private TextField portField;
    private TextField personalField;
    private TextField logField;
    private TextArea chatArea;
    private Button mainButton;
    
    private final int programWidth = 1000;
    private final int programHeight = 520;
    
    private Controller controller;
    private final ArrayList<String> users = new ArrayList<String>();
    
    @Override
    public void start(Stage arg01) {
        initializeComponents(arg01);
        initializeAnimation();
    }

    /**
     * Initializes all the gameStructures of user interface.
     * @param arg01 the stage to set the gameStructures to.
     */
    private void initializeComponents(Stage arg01) {
        arg01.setWidth(programWidth);
        arg01.setHeight(programHeight);
        arg01.setTitle("WhatsThat Client");
        arg01.setResizable(false);
       
        mainPane = new VBox();
        HBox hboxPane = new HBox();
        scene = new Scene(mainPane, programWidth, programHeight);
       
        //scene.setOnKeyPressed(e -> { System.out.println(e.getCode()); }); // send input to context manipulator
        
        Canvas canv = constructCanvasComponent();
        chatArea = constructChatAreaComponent();
        controller = new Controller(new Model(false), new View(canv.getGraphicsContext2D(), chatArea));
        hboxPane.getChildren().add(canv);
        hboxPane.getChildren().add(chatArea);
        mainPane.getChildren().add(hboxPane);
        
        // set up bottom components
        VBox vbottomPane = new VBox();
        FlowPane bottom = upperBottomComponent();
        FlowPane bBottom = lowerBottomComponent();
        vbottomPane.getChildren().add(bottom);
        vbottomPane.getChildren().add(bBottom);
        mainPane.getChildren().add(vbottomPane);
        
        arg01.setScene(scene);
        arg01.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
            	controller.terminateClientConnection();
            	Platform.exit();
                System.exit(0);
            }
        });
        arg01.show();
    }
    /**
     * 
     * 
     * @return
     */
    private Canvas constructCanvasComponent() {
    	Canvas tableCanvas = new Canvas(programWidth * componentCanvasWidthMulti, programHeight * heightMulti);
        context = tableCanvas.getGraphicsContext2D();
        context.setFill(Paint.valueOf("Grey"));
        context.fillRect(0, 0, programWidth * componentCanvasWidthMulti, programHeight * heightMulti);
        
        scene.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				context.setFill(Paint.valueOf("Grey"));
		        context.fillRect(0, 0, programWidth * componentCanvasWidthMulti, programHeight * heightMulti);
				context.setFill(Paint.valueOf("Red"));
				context.fillText(arg0.getSceneX() + ", " + arg0.getSceneY() + " -> " + arg0.getClickCount(), 50, 50);
			}
        });
        return tableCanvas;
    }
    /**
     * 
     * @return
     */
    public TextArea constructChatAreaComponent() {
    	TextArea area = new TextArea(Instructions.howToSetup());
        area.setPrefColumnCount(textColumns);
        area.setMinHeight(programHeight * heightMulti);
        area.setMaxHeight(programHeight * heightMulti);
        area.setPrefHeight(programHeight * heightMulti);

        area.setMinWidth(programWidth * componentWidthMulti);
        area.setMaxWidth(programWidth * componentWidthMulti);
        area.setPrefWidth(programWidth * componentWidthMulti);
        area.setEditable(false);
        area.setWrapText(true);
        return area;
    }
    /**
     * 
     * @return
     */
    private Button constructSubmitButtonComponent() {
    	Button submitButton = new Button("Submit");
        submitButton.setMinWidth(programWidth * (widthMulti * componentWidthMulti));
        submitButton.setMaxWidth(programWidth * (widthMulti * componentWidthMulti));
        submitButton.setPrefWidth(programWidth * (widthMulti * componentWidthMulti));
        
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!controller.isConnectedToServer() && !hasCorrectFields()) { return; }
				if (!controller.isConnectedToServer()) { initializeConnectionAction(); return; }
				if (controller.isConnectedToServer()) { 
					controller.sendChatMessage(personalField.getText().trim().replace("~", "")); 
					return; 
				}
			}	
        });
        return submitButton;
    }
    
    /**
     * 
     * @return
     */
    private TextField constructSubmissionFieldComponent() {
    	TextField submitField = new TextField();
        submitField.setMinWidth(programWidth * componentWidthMulti);
        submitField.setMaxWidth(programWidth * componentWidthMulti);
        submitField.setPrefWidth(programWidth * componentWidthMulti);
        submitField.positionCaret(submitField.getText().length());
        return submitField;
    }
    /**
     * 
     * @return
     */
    private FlowPane upperBottomComponent() {
        mainButton = constructSubmitButtonComponent();
        personalField = constructSubmissionFieldComponent();
    	FlowPane fp = new FlowPane();
        Label usernameLabel = new Label(" Username: ");
        usernameField = new TextField();
        Label addressLabel = new Label(" Address: ");
        addressField = new TextField(NetworkUtility.getHostAddress());
        Label portLabel = new Label("  Port: ");
        portField = new TextField(2757 + "");
        usernameField.setPrefColumnCount(textColumns);
        addressField.setPrefColumnCount(textColumns);
        portField.setPrefColumnCount(textColumns);
        
        Button leaveButton = constructLeaveButtonComponent();
        
        fp.getChildren().add(mainButton);
        fp.getChildren().add(personalField);
        fp.getChildren().add(usernameLabel);
        fp.getChildren().add(usernameField);
        fp.getChildren().add(addressLabel);
        fp.getChildren().add(addressField);
        fp.getChildren().add(portLabel);
        fp.getChildren().add(portField);
        fp.getChildren().add(leaveButton);
        return fp;
    }

    private FlowPane lowerBottomComponent() {
    	FlowPane fp = new FlowPane();
    	Button circleButton = new Button("Drop Circle");
    	Button squareButton = new Button("Drop Square");
    	Button trianlgeButton = new Button("Drop Triangle");
    	Button lineButton = new Button("Create Line");
    	Button pictureButton = new Button("Fetch Picture");
    	Button textButton = new Button("Write Text");
    	Label logLabel = new Label(" Log: ");
    	logField = new TextField();
    	
    	
    	fp.getChildren().add(circleButton);
    	fp.getChildren().add(squareButton);
    	fp.getChildren().add(trianlgeButton);
    	fp.getChildren().add(lineButton);
    	fp.getChildren().add(pictureButton);
    	fp.getChildren().add(textButton);
    	fp.getChildren().add(logLabel);
    	fp.getChildren().add(logField);
    	return fp;
    }
    
    private Button constructLeaveButtonComponent() {
    	Button b = new Button("Leave");
    	b.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				if (controller.isConnectedToServer()) {
					controller.terminateClientConnection();
				} else {
					chatArea.appendText("Nothing to disconnect from...\n");
				}
			}
    	});
    	return b;
    }
    /**
     * initializes the main loop of the game.
     */
    private void initializeAnimation() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
            	// TODO
            }
        }.start();
    }
    
    // checks username address and port fields.
    private boolean hasCorrectFields() {
    	if (usernameField.getText().length() < 1) {
    		chatArea.appendText("Username appears to be too short, please try again.\n");
    		return false;
    	}
    	if (NetworkUtility.isValidIP(addressField.getText()) == false) {
    		chatArea.appendText("Invalid IP Address, please try again.\n");
    		return false;
    	}
		if (NetworkUtility.isValidPort(Integer.valueOf(portField.getText())) == false) { 
			chatArea.appendText("Invalid Port, please try again.\n");
			return false; 
		}
		return true;
    }
    
    // start listening to server...
    private void listenForServer() {
    	Thread newThread = new Thread(new Client(chatArea, users, controller.getBufferedReader()));
    	newThread.start();
    }
    
    private void initializeConnectionAction() {
    	if (!controller.isConnectedToServer()) { return; }
    	
    	String endAddress = addressField.getText().trim().replace("~", "");
    	String username = usernameField.getText().trim().replace("~", "");
    	int portNumber = Integer.valueOf(portField.getText().trim().replace("~", ""));
    	
    	if (!NetworkUtility.isHostAvailable(endAddress, portNumber)) { 
    		chatArea.appendText("Server appears to be unavailable...\n");
    		return; 	
    	}
    	
		controller.initializeUsername(username);
		controller.initializeClientConnection(endAddress, portNumber);
		listenForServer();
    }
}
