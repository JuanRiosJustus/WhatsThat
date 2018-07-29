package dialogs;

import architecture.Controller;
import architecture.Metadata;
import architecture.Model;
import architecture.View;
import drawing.CanvasInterpreter;
import drawing.Colors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utlility.Instructions;
import utlility.JavaFXUtils;
import utlility.NetworkUtils;
import utlility.StringUtils;

public class ClientDialog extends Application {

    private GraphicsContext context;
    private Scene scene;
    private VBox mainPane;
    
    private TextField usernameField;
    private TextField addressField;
    private TextField portField;
    private TextField enterField;
    private TextArea chatArea;
    private Button mainButton;
    private Button colorButton;
    private HBox largerPane;
    
    private Controller controller;
    private CanvasInterpreter interpreter;
    private QueueDialog queueDlg;

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
        arg01.setWidth(Constants.UI_PROGRAM_WIDTH);
        arg01.setHeight(Constants.UI_PROGRAM_HEIGHT);
        arg01.setMaxWidth(Constants.UI_PROGRAM_WIDTH);
        arg01.setMaxHeight(Constants.UI_PROGRAM_HEIGHT);
        arg01.setTitle(Constants.PROGRAM_NAME);
        arg01.setResizable(false);
       
        mainPane = new VBox();
        largerPane = new HBox();
        scene = new Scene(mainPane, Constants.UI_PROGRAM_WIDTH, Constants.UI_PROGRAM_HEIGHT);
        //scene.setOnKeyPressed(e -> { System.out.println(e.getCode()); }); // send input to context manipulator
        
        Canvas canv = initMainComponents();
        chatArea = constructChatAreaComponent();
        controller = new Controller(new Model(false), new View(canv.getGraphicsContext2D(), chatArea));

        largerPane.getChildren().add(canv);
        largerPane.getChildren().add(chatArea);
        mainPane.getChildren().add(largerPane);

        
        // set up bottom components
        VBox bottomPane = new VBox();
        FlowPane upper = upperBottomComponent();
        FlowPane middle = middleBottomComponent();
        FlowPane bottom = bottomBottomComponent();
        bottomPane.getChildren().add(upper);
        bottomPane.getChildren().add(middle);
        bottomPane.getChildren().add(bottom);
        mainPane.getChildren().add(bottomPane);

        arg01.setScene(scene);
        arg01.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
            	//controller.terminateClientConnection();
            	Platform.exit();
                System.exit(0);
            }
        });
        arg01.show();
    }
    /**
     * Constructs the main graphics components for the client.
     * @return the graphical component.
     */
    private Canvas initMainComponents() {
    	Canvas tableCanvas = MainComponentDialogs.getCanvasInstance();
        interpreter = new CanvasInterpreter(MainComponentDialogs.getContext());
        scene.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent arg0) {
			    if (arg0.getSceneY() < 0 || arg0.getSceneY() > Constants.UI_CANVAS_HEIGHT) { return; }
			    if (arg0.getSceneX() < 0 || arg0.getSceneX() > Constants.UI_CANVAS_WIDTH) { return; }
			    if (largerPane.getChildren().get(0) != MainComponentDialogs.getCanvasInstance()) { return; }

			    // Check if there is an image to be sent
                if (queueDlg.hasImage()) { enterField.setText(queueDlg.extractImage()); }
			    interpreter.handleEvent(arg0, enterField, colorButton.getText());
			    controller.sendCanvasObjectToNetwork(interpreter.constructStringFromCanvasObjects()); // may potential break
                controller.sendCanvasObjectToNetwork(interpreter.constructConsumableCanvasObject());
			}
        });

        return tableCanvas;
    }
    /**
     * Constructs the main chat area for the user.
     * @return returns the char area constructs.
     */
    private TextArea constructChatAreaComponent() {
    	TextArea area = new TextArea(Instructions.howToSetup());
        area.setMinHeight(Constants.UI_CHATAREA_HEIGHT);
        area.setMaxHeight(Constants.UI_CHATAREA_HEIGHT);
        area.setPrefHeight(Constants.UI_CHATAREA_HEIGHT);

        area.setMinWidth(Constants.UI_CHATAREA_WIDTH);
        area.setMaxWidth(Constants.UI_CHATAREA_WIDTH);
        area.setPrefWidth(Constants.UI_CHATAREA_WIDTH);
        area.setEditable(false);
        area.setWrapText(true);
        return area;
    }
    /**
     * Logic to construct the enter button and makeup on it.
     * @return The enter button to submit held information from the UI.
     */
    private Button constructEnterButtonComponent() {
    	Button enterButton = JavaFXUtils.createButton(Constants.ENTER_BUTTON_TEXT, 1);
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				
				if (controller.isConnected()) {
				    controller.sendMessageToServer(enterField.getText());
					enterField.clear();
					return;
				} else {
				    JavaFXUtils.threadSafeAppendToTextArea(chatArea, Constants.ERROR_UNABLE_TOCONNECT);
                }
			}	
        });
        return enterButton;
    }

    /**
     * Constructs a connection button used to close and join the server
     * @return the button to connect to and join the server.
     */
    private Button constructConnectionButton() {
        Button connectionButton = JavaFXUtils.createButton(Constants.CONNECTION_BUTTON_TEXT, 1);
        connectionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (controller.isConnected() == false) {
                    initializeConnectionAction();
                } else {
                    initializeDisconnectionAction();
                }
            }
        });
        return connectionButton;
    }
    /**
     * Constructs the enter field component that lays near the
     * enter button, this field is the main field to send messages.
     * @return the text field for most message submissions
     */
    private TextField constructEnterFieldComponent() {
    	TextField enterField = new TextField(Constants.MESSAGE_FIELD_PROMPT);
        enterField.setMinWidth(Constants.UI_LESSER_COMP_SIZE * 2);
        enterField.setMaxWidth(Constants.UI_LESSER_COMP_SIZE * 2);
        enterField.setPrefWidth(Constants.UI_LESSER_COMP_SIZE * 2);
        enterField.positionCaret(enterField.getText().length());
        enterField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                enterField.clear();
            }
        });
        return enterField;
    }

    private Button constructFetchPicturesButton() {
        Button btn = JavaFXUtils.createButton(Constants.FETCH_BUTTON_TEXT, 1);
        queueDlg = new QueueDialog();
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // constructs queue of images from current html on engine
                // user can select image to post on canvas
                queueDlg.createImagesDialog(chatArea, MainComponentDialogs.getBrowserInstance().getEngine().getLocation());
            }
        });

        return btn;
    }

    /**
     * The upper most set of components on the UI on the bottom.
     * @return flow pane containing button components.
     */
    private FlowPane upperBottomComponent() {
        FlowPane fp = new FlowPane();
        mainButton = constructEnterButtonComponent();
        enterField = constructEnterFieldComponent();

        fp.getChildren().add(mainButton);
        fp.getChildren().add(enterField);
        fp.getChildren().add(constructFetchPicturesButton());
        return fp;
    }
    /**
     * Constructs the address field for the user.
     * @return the textfield representing the address.
     */
    private TextField constructAddressFieldComponent() {
        TextField addressField = new TextField(Constants.ADDRESS_FIELD_PROMPT);
        addressField.setMaxWidth(Constants.UI_LESSER_COMP_SIZE);
        addressField.setMinWidth(Constants.UI_LESSER_COMP_SIZE);
        addressField.setPrefWidth(Constants.UI_LESSER_COMP_SIZE);
        addressField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.hasAttemptedAdjustment();
                if (!controller.isConnected()) {
                    addressField.clear();
                }
            }
        });
        return addressField;
        // add a editable field for the user to connect to the server.
    }
    /**
     * Constructs the field that represents the users port.
     * @return the field representing the users port
     */
    private TextField constructPortFieldComponent() {
        TextField portField = new TextField(Constants.PORT_FIELD_PROMPT);
        portField.setMinWidth(Constants.UI_LESSER_COMP_SIZE);
        portField.setPrefWidth(Constants.UI_LESSER_COMP_SIZE);
        portField.setMaxWidth(Constants.UI_LESSER_COMP_SIZE);
        portField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.hasAttemptedAdjustment();
                if (!controller.isConnected()) {
                    portField.clear();
                }
            }
        });
        return portField;
    }

    /**
     * Main button used to switch interface
     * @return the switch button
     */
    private Button constructModeButton() {
        Button exploreButton = JavaFXUtils.createButton(Constants.MODE_BUTTON_TEXT, 1);
        exploreButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (largerPane.getChildren().get(0) == MainComponentDialogs.getCanvasInstance()) {
                    largerPane.getChildren().set(0, MainComponentDialogs.getBrowserInstance());
                    MainComponentDialogs.getBrowserInstance().getEngine().load(Constants.DEFAULT_WEBSITE);
                } else {
                    largerPane.getChildren().set(0, MainComponentDialogs.getCanvasInstance());
                }
            }
        });
        return exploreButton;
    }

    /**
     * Consturcts the button to clear the main canvas.
     * @return clear button
     */
    private Button constructClearButton() {
        Button clearButton = JavaFXUtils.createButton(Constants.CLEAR_BUTTON_TEXT, 1);
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainComponentDialogs.clearCanvas();
            }
        });
        return clearButton;
    }

    private Button constructColorButton() {
        Button btn = JavaFXUtils.createButton(Colors.BLACK.toString(), 1);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            int current = 0;
            @Override
            public void handle(ActionEvent event) {
                current++;
                if (current + 1 > Colors.values().length) { current = 0; }
                btn.setText(Colors.values()[current].toString());
                interpreter.SetColor(btn.getText());
            }
        });
        return btn;
    }

    private Button constructSaveButton() {
        Button btn = JavaFXUtils.createButton(Constants.SAVE_BUTTON_TEXT, 1);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            String[] exts = new String[] { "PNG", "JPG" };
            @Override
            public void handle(ActionEvent event) {
                JavaFXUtils.saveCanvasDialog(MainComponentDialogs.getCanvasInstance(), exts, ".");
            }
        });

        return btn;
    }
    /**
     * Represents the bottom most component of the input component at the
     * bottom of the users client.
     * @return the panel containing all tbe buttons of the lower most bottom component.
     */
    private FlowPane middleBottomComponent() {
    	FlowPane fp = new FlowPane();
    	
    	fp.getChildren().add(constructModeButton());
    	colorButton = constructColorButton();
    	fp.getChildren().add(colorButton);
    	fp.getChildren().add(constructClearButton());
    	fp.getChildren().add(constructSaveButton());
    	return fp;
    }
    private TextField constructUsernameFieldComponent() {
        TextField usernameField = new TextField(Constants.USERNAME_FIELD_PROMPT);
        usernameField.setMaxWidth(Constants.UI_LESSER_COMP_SIZE);
        usernameField.setMinWidth(Constants.UI_LESSER_COMP_SIZE);
        usernameField.setPrefWidth(Constants.UI_LESSER_COMP_SIZE);
        usernameField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.hasAttemptedAdjustment();
                if (!controller.isConnected()) {
                    usernameField.clear();
                }
            }
        });
        return usernameField;
    }
    private FlowPane bottomBottomComponent() {
        FlowPane fp = new FlowPane();
        portField = constructPortFieldComponent();
        fp.getChildren().add(portField);
        addressField = constructAddressFieldComponent();
        fp.getChildren().add(addressField);
        usernameField = constructUsernameFieldComponent();
        fp.getChildren().add(usernameField);
        fp.getChildren().add(constructConnectionButton());

        return fp;
    }
    /**
     * initializes the main loop of the game.
     */
    private void initializeAnimation() {
        controller.handleCanvasStream();
    }

    // checks username address and port fields.
    // TODO needs to have a defualt ip and potentially port
    private boolean hasCorrectFields() {

        // TODO
        // if user never entered the settings bar, use default
        // add all information from the field to a metadata item
        // add metadata to controller
        // check if controller metadata is correct
        //
        if (controller.getHasAttemptedAdjustment() == false) { return true; }

        // check address
        if (NetworkUtils.isValidIP(addressField.getText()) == false) {
            chatArea.appendText(Constants.ERROR_INVALIDIP + "\n");
            return false;
        }
        // check port
        if (StringUtils.isNumeric(portField.getText()) == false) {
            chatArea.appendText(Constants.ERROR_INVALIDPORT + "\n");
            return false;
        }
        if (NetworkUtils.isValidPort(Integer.valueOf(portField.getText())) == false) {
            chatArea.appendText(Constants.ERROR_INVALIDPORT + "\n");
            return false;
        }
        // check the user name
        if (usernameField.getText().length() == 1 && usernameField.getText().equalsIgnoreCase(Constants.RANDOMNAME_INDICATOR)) {
            chatArea.appendText(Constants.ERROR_RANDOMNAME_INDICATOR + "\n");
            usernameField.setText(StringUtils.generateRandomName(Constants.RANDOM_NAME_SIZE));
            return true;
        } else if (usernameField.getText().length() > 0 && !StringUtils.containsDelimiter(usernameField.getText())) {
            return true;
        } else {
            chatArea.appendText(Constants.ERROR_NAMETOOSHORT + "\n");
            return false;
        }
    }

    /**
     * Initializes the client server connection within the controller and disables
     * controls, effectively finalizing user changes to connection information
     */
    private void initializeConnectionAction() {

        if (!hasCorrectFields()) { return; }

        String username = controller.getName();
        String endAddress = controller.getEndIP();
        int portNumber = controller.getPort();
        if (controller.getHasAttemptedAdjustment()) {
            username = usernameField.getText().trim();
            endAddress = addressField.getText().trim();
            portNumber = Integer.valueOf(portField.getText().trim());
            controller.setMetadata(new Metadata(username, endAddress, portNumber));
        }

		controller.initializeClientConnectionToServer(chatArea, usernameField, endAddress, portNumber);
		addressField.setEditable(false);
		usernameField.setEditable(false);
		portField.setEditable(false);

		// last check to see if controller is connect
        if (controller.isConnected()) { return; }
        addressField.setEditable(true);
        usernameField.setEditable(true);
        portField.setEditable(true);
    }

    /**
     * Disconnects the user from their current connection.
     */
    private void initializeDisconnectionAction() {

        controller.terminateClientConnection(chatArea);
        addressField.setEditable(true);
        usernameField.setEditable(true);
        portField.setEditable(true);
    }
}
