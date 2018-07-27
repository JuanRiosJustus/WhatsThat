package architecture;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import messaging.*;
import network.*;
import dialogs.Constants;
import responsesRequests.GameStartRequest;
import utlility.NetworkUtils;
import utlility.TypeMatcher;

public class Controller {
	
	private Model model;
	private View view;
	
	/* Client Specific instance variables */
	private Socket connection;
	private BufferedReader reader;
	private PrintWriter writer;
	private boolean connected;
	private boolean hasFinalizedUsername;
	private boolean attemptedAdjustment;
	private String username;
	
	private boolean isRunning;
	private Thread mainThread;
	private Client mainClient;
	private Server mainServer;
	private Thread mainBackgroundThread;
	private Metadata networkInfo;
	
	/* Input and Output Helper Class*/
	private IOUHolder iouholder;
	
	public Controller(Model newModel, View newView) {
		model = newModel;
		view = newView;
		iouholder = new IOUHolder();
		networkInfo = new Metadata(
                Constants.DEFAULT_NAME,
                NetworkUtils.getHostAddress(),
                Integer.valueOf(Constants.DEFAULT_PORT));
	}

    /**
     * Handles the input of the given graphics
     */
	public void handleCanvasStream() {
	    Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) { view.handleCanvasInput(iouholder); }
            }
        });
	    t.start();
    }

    /**
     * Sends given input back to the server to be sent to the clients.
     */
	public void handleIOStream() {
		if (iouholder.getQueue().containedIncomingElements() == 0) { return; }
        Object obj = iouholder.getQueue().dequeue();
        if (TypeMatcher.isInstanceOf(obj, GameStartRequest.class)) {
            String response = model.handleGameStartRequest((GameStartRequest) obj);
            sendGameState();
            iouholder.getQueue().enqueueFromInternal(response);
        }
	}
	public void sendGameState() {
        iouholder.getQueue().enqueueFromInternal("*****************************");
        iouholder.getQueue().enqueueFromInternal("TOPIC: " + model.getGameTopic());
        iouholder.getQueue().enqueueFromInternal("LEADER: " + model.getGameLeader());
        iouholder.getQueue().enqueueFromInternal("PLAYERS: " + model.getGamePlayers());
        iouholder.getQueue().enqueueFromInternal("******************************");
    }
    /**
     * Starts the server connection such that conenctions to clients can be established.
     * @param mainArea area to append information to.
     * @param port port to start the server on.
     * @return true if and only if the server thread was started.
     */
	public boolean initializeServerConnectionToClients(TextArea mainArea, int port) {
	    if (isRunning == false) {
	        mainServer = new Server(iouholder, mainArea, port);
	        mainThread = new Thread(mainServer);
	        mainThread.start();
	        mainArea.appendText("Server has started.\n");
	        isRunning = true;
	        initializeBackgroundThreads();
        } else {
	        mainArea.appendText("Server is already running.\n");
	    }
	    return isRunning;
    }

    /**
     * Starts th emain background thread for the server.
     */
	private void initializeBackgroundThreads() {
		mainBackgroundThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning) { // ensure that the server is running
					handleIOStream();
				}
			}
		});
		mainBackgroundThread.start();
	}

    /**
     * Announces the given message to all users found within the underlying map.
     * @param mainArea area to append information to.
     * @param message message to send to everyone.
     */
    public void announceFromServer(TextArea mainArea, String message) {
        Iterator<Map.Entry<String, User>> it = iouholder.getUsers().entrySet().iterator();
        Message msg = MessageUtils.constructMessage("SERVER", message, MessageType.Public);
        while (it.hasNext()) {
            try {
                Map.Entry<String, User> r = it.next();
                r.getValue().sendMessage(msg.build());
            } catch (Exception ex) {
                mainArea.appendText(Constants.ERROR_UNABLE_TOSEND_MESSAGE + "\n");
            }
        }
        mainArea.positionCaret(mainArea.getText().length());
    }

    /**
     * Appends all information about the the status of the users to the given area.
     * @param mainArea given area to append information to.
     */
    public void appendUsersToArea(TextArea mainArea) {
        mainArea.appendText("\n=========={ CURRENT USERS }==============\n");
        Iterator<Map.Entry<String, User>> users = iouholder.getUsers().entrySet().iterator();
        while (users.hasNext()) {
            User currentUser = users.next().getValue();
            mainArea.appendText("[" + currentUser.getName() + "] (is host: " + currentUser.isHost() + ")\n");
        }
        mainArea.appendText("\n==========================================\n");
    }
	/**
	 * Initializes the inner connection within the conroller view model given 
	 * the port and address to  connect to.
	 * @param address the end address to connect to.
	 * @param port the port number to use.
	 * @return true if and only if the initialization was successfull
	 */
	public boolean initializeClientConnectionToServer(TextArea ta, TextField tf, String address, int port) {
		if (connected) { return false; }
	    try {
			connection = new Socket(networkInfo.getEndIP(), networkInfo.getPort());
			InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			reader = new BufferedReader(isr);
			writer = new PrintWriter(connection.getOutputStream());
			Message msg = MessageUtils.constructMessage(networkInfo.getName(), Constants.HAS_CONNECTED_PROMPT, MessageType.Connect);
			writer.println(msg.build());
			writer.flush();
			view.appendTextToChat(Constants.CONNECTED_TO_SERVER_PROMPT + "\n");
			connected = true;
			listenToTheServer(ta);
			listenForFinalUsername(tf);
			return true;
		} catch (Exception ex) {
			view.appendTextToChat(ex.getMessage());
			view.appendTextToChat(Constants.ERROR_UNABLE_TOCONNECT + "\n");
			return false;
		}
	}

    /**
     * Checks for the name that the server will use for the client.
     * @param tf the field to set the given username as.
     */
	private void listenForFinalUsername(TextField tf) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (hasFinalizedUsername == false) {
                    finalizeUsername(tf);
                }
            }
        }).start();
    }

    private void handleClientState() {
	    new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning) {

                }
            }
        }).start();
    }

    /**
     * Operation to set the username of the client to the given textfield
     * @param tf the field to set the contents given from the server as.
     */
    private void finalizeUsername(TextField tf) {
	    if (iouholder.getQueue().containsIncomingElements() == false) { return; }
	    String rawMessageString = (String) iouholder.getQueue().dequeue();
        setUsername(rawMessageString);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tf.setText(username);
            }
        });
        hasFinalizedUsername = true;
	}

    /**
     * Sends a message to the underlying server.
     * @param messageContent contents of the message.
     */
	public void sendMessageToServer(String messageContent) {
	    mainClient.sendStringToNetwork(writer, messageContent, username);
    }

    /**
     * This accepts mutliple canvasObjects
     * @param points
     */
    public void sendCanvasObjectToNetwork(String points) {
        if (points == null || points.length() < 1) { return; }
        if (mainClient == null) { return; }
	    mainClient.sendCanvasObjectsToNetwork(writer, points, networkInfo.getName());
    }

	private void listenToTheServer(TextArea mainArea) {
	    mainClient = new Client(mainArea, iouholder, reader, username);
	    mainThread = new Thread(mainClient);
	    mainThread.start();
    }

    // TODO FIX THE METHODS BELOW
	public void terminateClientConnection(TextArea mainArea) {
		if (connected == false) {
			mainArea.appendText(Constants.ERROR_ALREADUDISCONNECTED + "\n");
			return;
		}
		sendDisconnectMessage();
		disconnect();
	}

	private void sendDisconnectMessage() {
		try {
		    Message msg = MessageUtils.constructMessage(username, "has disconnected.", MessageType.Disconnect);
			writer.println(msg.build());
			writer.flush();
		} catch (Exception ex) {
			view.appendTextToChat("Error sending disconnect message...");
		}
	}
	private void disconnect() {
		try {
			connection.close();
			connected = false;
			view.appendTextToChat("Disconnected from server.");
		} catch (Exception ex) {
			view.appendTextToChat("Error disconnecting from server...");
		}
	}
    public void stopServerAction(TextArea ta) {
        try {
            announceFromServer(ta,"Server: is stopping and all users will be disconnected");
            ta.appendText("Server stopping... \n");
            isRunning = false;
            forceDisconnections();
            //Platform.exit();					TODO
            //System.exit(0);					TODO
        } catch (Exception ex) {
            ta.appendText("Error stopping Server... \n");
        }
    }
    private void forceDisconnections() {
		Iterator<Map.Entry<String, User>> it = iouholder.getUsers().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, User> et = it.next();
			Message msg = MessageUtils.constructMessage("SERVER", "Server closing", MessageType.Public);
			et.getValue().sendMessage(msg.build());
			iouholder.getUsers().remove(et.getKey());
		}
	}

	/**
	 * Given a field of arguments, executes the appropriate task
	 * @param tf text field where the commands are given
	 * @param ta one of the influenced components
	 */
	public void handleServerCommand(TextField tf, TextArea ta) {
		String cmd = tf.getText();
		if (cmd.equalsIgnoreCase(Constants.CLEAR_COMMAND)) {
			ta.clear();
		}

	}
	public void setUsername(String name) { username = name; }
	public boolean isConnected() { return connected; }
	public void setMetadata(Metadata data) {
	    attemptedAdjustment = true;
	    networkInfo = data;
	}
	public boolean getHasAttemptedAdjustment() { return attemptedAdjustment; }
	public int getPort() { return networkInfo.getPort(); }
	public String getEndIP() { return networkInfo.getEndIP(); }
	public String getName() { return networkInfo.getName(); }
}
