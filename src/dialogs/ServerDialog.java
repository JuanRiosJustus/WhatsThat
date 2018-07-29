package dialogs;

import architecture.Controller;
import architecture.Model;
import architecture.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import messaging.Message;
import messaging.MessageUtils;
import messaging.MessageType;
import utlility.NetworkUtils;
import utlility.StringUtils;

public class ServerDialog extends Application {

	private BorderPane mainPane;
	private Scene mainScene;
	private TextArea mainArea;
	private TextField msgField;
	private TextField mainField;
	private ComboBox<String> playersCombobox;
	
	private Controller controller;

	@Override
	public void start(Stage arg0) {
		initializeComponents(arg0);
	}

	private void initializeComponents(Stage mainStage) {
		mainStage.setWidth(Constants.UI_PROGRAM_WIDTH);
		mainStage.setHeight(Constants.UI_PROGRAM_HEIGHT);
		mainStage.setTitle(Constants.PROGRAM_NAME);
		mainStage.setResizable(false);
		mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override public void handle(WindowEvent arg0) {
				Platform.exit();
				System.exit(0);
			}
		});
		
		mainPane = new BorderPane();
		mainScene = new Scene(mainPane, Constants.UI_PROGRAM_WIDTH, Constants.UI_PROGRAM_HEIGHT);
		
		mainArea = constructMainTextArea();
		mainArea.setEditable(false);
		mainPane.setTop(mainArea);
		controller = new Controller(new Model(true), new View(null, mainArea));
		
		FlowPane fp = initializeButtonComponents();
		mainPane.setCenter(fp);
		
		mainStage.setScene(mainScene);
		mainStage.show();
	}

    /**
     * Constructs the main chat area for the server where all the input is displayed
     * @return the text area
     */
	private TextArea constructMainTextArea() {
		TextArea area = new TextArea(NetworkUtils.getHostAddresses());
		area.setMaxHeight(Constants.UI_CHATAREA_HEIGHT);
		area.setPrefHeight(Constants.UI_CHATAREA_HEIGHT);
		area.setMinHeight(Constants.UI_CHATAREA_HEIGHT);

		area.setMaxWidth(Constants.UI_PROGRAM_WIDTH);
		area.setPrefWidth(Constants.UI_PROGRAM_WIDTH);
		area.setMinWidth(Constants.UI_PROGRAM_WIDTH);

		return area;
	}

    /**
     * Consturcts the server button where once pressed,
     * starts listening to ongoing connections.
     * @return the server start button.
     */
	private Button constructStartServerButton() {
		Button startButton = new Button(Constants.START_BUTTON_TEXT);
		startButton.setMinWidth(Constants.UI_LESSER_COMP_SIZE);
		startButton.setPrefWidth(Constants.UI_LESSER_COMP_SIZE);
		startButton.setMaxWidth(Constants.UI_LESSER_COMP_SIZE);
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				if (controller.isConnected() || !hasCorrectFields()) { return; }

				int port = Integer.valueOf(mainField.getText());
				controller.initializeServerConnectionToClients(mainArea, port);
				msgField.setEditable(true);
			}
		});
		return startButton;
	}

    /**
     * Constructs the message button which sends information from the server input
     * and sends it to the currently connected clients.
     * @return the message button.
     */
	private Button constructMessageButton() {
        Button msgButton = new Button(Constants.SEND_BUTTON_TEXT);
        msgButton.setMinWidth(Constants.UI_LESSER_COMP_SIZE);
        msgButton.setPrefWidth(Constants.UI_LESSER_COMP_SIZE);
        msgButton.setMaxWidth(Constants.UI_LESSER_COMP_SIZE);
        msgButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
                if (StringUtils.isValidMessage(msgField.getText()) == false) { return; }
                Message msg = MessageUtils.constructMessage(NetworkUtils.getHostAddress(), msgField.getText(), MessageType.Public);
                controller.announceFromServer(mainArea, msg.build());
                msgField.clear();
            }
        });
        return msgButton;
    }

	/**
	 * Constructs the stop button which haults server from listening
	 * @return stop button
	 */
	private Button constructStopServerButton() {
		Button stopButton = new Button(Constants.STOP_BUTTON_TEXT);
        stopButton.setMinWidth(Constants.UI_LESSER_COMP_SIZE);
        stopButton.setPrefWidth(Constants.UI_LESSER_COMP_SIZE);
        stopButton.setMaxWidth(Constants.UI_LESSER_COMP_SIZE);
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				controller.stopServerAction(mainArea);
			}
		});
		return stopButton;
	}

	/**
	 * Button used to check the status of current users on the server
	 * @return the button to get the online users.
	 */
	private Button constructUsersButton() {
		Button usersButton = new Button(Constants.USERS_BUTTON_TEXT);
        usersButton.setMinWidth(Constants.UI_LESSER_COMP_SIZE);
        usersButton.setPrefWidth(Constants.UI_LESSER_COMP_SIZE);
        usersButton.setMaxWidth(Constants.UI_LESSER_COMP_SIZE);
		usersButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				controller.appendUsersToArea(mainArea);
			}
		});
		return usersButton;
	}

    /**
     * Constructs the clear button such that the log will be cleared
     * @return the clear button
     */
	private Button constructCommandButton(TextField tf) {
        Button clearButton = new Button(Constants.COMMAND_BUTTON_TEXT);
        clearButton.setMinWidth(Constants.UI_LESSER_COMP_SIZE);
        clearButton.setPrefWidth(Constants.UI_LESSER_COMP_SIZE);
        clearButton.setMaxWidth(Constants.UI_LESSER_COMP_SIZE);
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
            	controller.announceFromServer(mainArea, tf.getText());
            }
        });
        return clearButton;
    }

    /**
     * Constructs the main input field from the user ui
     * @return the field
     */
    private TextField constructMainField() {
	    TextField tf = new TextField(Constants.DEFAULT_PORT);
	    tf.setMinWidth(Constants.UI_LESSER_COMP_SIZE);
	    tf.setPrefWidth(Constants.UI_LESSER_COMP_SIZE);
	    tf.setMaxWidth(Constants.UI_LESSER_COMP_SIZE);
	    return tf;
    }
    /**
     * Constructs a pane with all the bottom most components for the user.
     * @return the pane with its components.
     */
	private FlowPane initializeButtonComponents() {
		FlowPane fp = new FlowPane();
		// ----------------------------- components to send to all clients -----------------------/
		msgField = new TextField();
		msgField.setEditable(false);
		Button msgButton = constructMessageButton();
		Button startButton = constructStartServerButton();
		Button stopButton = constructStopServerButton();
		Button checkUsersButton = constructUsersButton();
        mainField = constructMainField();
		Button commandButton = constructCommandButton(msgField);

		fp.getChildren().add(startButton);
		fp.getChildren().add(stopButton);
		fp.getChildren().add(checkUsersButton);
		fp.getChildren().add(commandButton);
		//fp.getChildren().add(portLabel);
		fp.getChildren().add(mainField);
		//fp.getChildren().add(msgField);
		//fp.getChildren().add(msgButton);
		return fp;
	}

	/**
	 * Checks to see if the server contains the correct field to listen
	 * to connections correctly
	 * @return true if and only if the port o the server is correct.
	 */
	private boolean hasCorrectFields() {

		// Check if the current port number is correct
		String portData = mainField.getText();
		if (StringUtils.isNumeric(portData) == false) {
			mainArea.appendText(Constants.ERROR_INVALIDPORT + "\n");
			return false;
		}

		int port = Integer.valueOf(portData).intValue();
		if (NetworkUtils.isValidPort(port) == false) {
			mainArea.appendText(Constants.ERROR_INVALIDPORT + "\n");
			return false;
		}

		mainArea.appendText("PORT: " + portData + "\n");
		mainArea.appendText("IP: " + NetworkUtils.getHostAddress() + "\n");

		return true;
	}
}
