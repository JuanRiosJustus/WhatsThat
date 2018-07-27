package dialogs;

import network.Network;
import utlility.NetworkUtils;

import java.awt.*;

public class Constants {

    /* MAIN APPLICATION CONSTANTS */
    public static final String PROGRAM_NAME = "What's That?";
    public static final String DEFAULT_NAME = "defaultName";
    public static final String DEFAULT_PORT = "2757";
    public static final String DEFAULT_WEBSITE = "https://www.google.com";
    private static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    public static final double UI_PROGRAM_WIDTH = (screen.getWidth() * .75);
    public static final double UI_PROGRAM_HEIGHT = (screen.getHeight() * .75);

    public static final double UI_CANVAS_WIDTH = UI_PROGRAM_WIDTH * .75;
    public static final double UI_CANVAS_HEIGHT = UI_PROGRAM_HEIGHT * .80;

    public static final double UI_CHATAREA_WIDTH = UI_PROGRAM_WIDTH * .25;
    public static final double UI_CHATAREA_HEIGHT = UI_PROGRAM_HEIGHT * .80;

    public static final double UI_LESSER_COMP_SIZE = UI_PROGRAM_WIDTH * .2;
    public static final int RANDOM_NAME_SIZE = 7;

    /* SPECIAL CHARACTERS */
    public static final char DELIMITER = '|';
    public static final char CANVAS_OBJECT_DELIMITER = '~';
    public static final String EMPTY_STRING = "";

    /* UI CONSTANTS */

    public static final String MESSAGE_FIELD_PROMPT = "Text here...";
    public static final String USERNAME_FIELD_PROMPT = "What's your name?";
    public static final String PORT_FIELD_PROMPT = "Used PORT: " + DEFAULT_PORT;
    public static final String HAS_CONNECTED_PROMPT = "Has connected";
    public static final String CONNECTED_TO_SERVER_PROMPT = "Connected to server";
    public static final String ADDRESS_FIELD_PROMPT = "Endpoint IPV4: " + NetworkUtils.getHostAddress();

    /* BUTTON TEXT */
    public static final String ENTER_BUTTON_TEXT = "Enter";
    public static final String CONNECTION_BUTTON_TEXT = "Connect/Disconnect";
    public static final String START_BUTTON_TEXT = "Start";
    public static final String SEND_BUTTON_TEXT = "Send";
    public static final String STOP_BUTTON_TEXT = "Stop";
    public static final String USERS_BUTTON_TEXT = "Users";
    public static final String MODE_BUTTON_TEXT = "Mode";
    public static final String QUEUE_BUTTON_TEXT = "Queue";
    public static final String SETTINGS_BUTTON_TEXT = "Settings";
    public static final String PORT_LABEL_TEXT = "Port:";
    public static final String ADDRESS_LABEL_TEXT = "Address:";
    public static final String USERNAME_LABEL_TEXT = "\tUsername:";

    /* SERVER INPUT COMMANDS */
    public static final String COMMAND_BUTTON_TEXT = "Command";
    public static final String CLEAR_COMMAND = "Clear";

    /* ERRORES */
    public static final String ERROR_NAMETOOSHORT = "Name appears to be too short";
    public static final String RANDOMNAME_INDICATOR = "?";
    public static final String ERROR_RANDOMNAME_INDICATOR = "Constructing Random name";
    public static final String ERROR_INVALIDPORT = "Invalid port detected.";
    public static final String ERROR_INVALIDIP = "Invalid IP Address";
    public static final String ERROR_UNABLE_TOCONNECT = "Unable to connect to server...";
    public static final String ERROR_CLIENT_DISCONNECTED = "Client has been disconnected...";
    public static final String ERROR_ALREADYCONNECTED = "Already connected";
    public static final String ERROR_ALREADUDISCONNECTED = "Already disconnected";
    public static final String ERROR_UNABLE_TOSEND_MESSAGE = "Unable to send message";

    /* MESSAGING CONSTANTS */
    public static final String REGEX_FOR_MESSAGE_DELIMITER = "\\|";
    public static final String REGEX_FOR_COMMAND_DELIMITER = "\\s+";
    public static final int DELIMITERS_FOR_MESSAGE = 2;
    //public static final int DELIMITERS_FOR_GAM
    public static final int PEN_SIZE = 4;
    //  STRING_COMMAND_TOKEN(0) STRING_COMMAND_NAME(1) STRING_TOPIC(2) INT_ROUNDS(3)
    public static final int COMMAND_GAMESTART_PARAMETERS = 4;


}
