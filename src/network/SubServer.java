package network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import Logging.Log;
import commands.CommandHandler;
import dialogs.Constants;
import game.Stopwatch;
import javafx.scene.control.TextArea;
import messaging.*;
import utlility.JavaFXUtils;

public class SubServer extends NetworkNode implements Runnable {

	private TextArea mainArea;
	private PrintWriter writer;
	private Network network;
	private CommandHandler handler;
	private Thread gameFeedbackThread;
	
	public SubServer(IOUHolder iou, InputStream s, PrintWriter pw, TextArea a) {
		super(new BufferedReader(new InputStreamReader(s)), iou);
		writer = pw;
		mainArea = a;
		network = new Network();
		handler = new CommandHandler();
	}

    /**
     * Handles the stream of input incoming connections
     */
	@Override
	public void run() {
		String message = null;
		Message msg = null;
		MessageType type;
		usermap.put("GAME", new User("GAME"));
		usermap.put("SERVER", new User("SERVER"));
		
		try {
			while ((message = input.readLine()) != null) {
                msg = MessageUtils.parseMessageBuild(message);
                type = MessageUtils.getMessageType(msg.getMessageTypeValueAsString());

                // check if the last message was a draw and the timer has not past the delay
                JavaFXUtils.threadSafeAppendToTextAreaForServer(mainArea, msg);

				switch(type) {
					case Connect: {
						handleUserJustConnectedAction(msg);
					} break;
					case Disconnect: {
						handleUserJustDisconnectedAction(usermap.get(msg.getAuthor()), msg);
					} break;
					case Public: {
						network.announceMessagePublicly(usermap, msg);
					} break;
					case Command: {
					    // TODO this is unmaintained, and can be scrapped/ or implemented.
						// Commands are invoked as >Command >CommandName >CommandParams
                        JavaFXUtils.threadSafeAppendToTextArea(mainArea, "Parsing command...");
                        //appendStringToMainAreaRunLater("Attempting to parse command...");
						//Message response = handler.handleCommand(usermap, ioqueue, msg, usermap.get(msg.getAuthor()).isHost());
                        //handleParticularResponseRoutine(response);
						//Message mssg = MessageUtils.constructMessage("SERVER", response.getContent(), MessageType.Public);
						//network.announceMessagePublicly(usermap, mssg);
						//appendStringToMainAreaRunLater("Command has been parsed.");
					} break;
                    case Draw: {
                        network.announceMessagePublicly(usermap, msg);
                    } break;
					default: {
						// TODO man, this needs to be done later...
                        JavaFXUtils.threadSafeAppendToTextArea(mainArea, message);
					} break;
				}
			}
		} catch (Exception ex) {
		    JavaFXUtils.threadSafeAppendToTextArea(mainArea, Constants.ERROR_CLIENT_DISCONNECTED);
			JavaFXUtils.threadSafeAppendToTextArea(mainArea, ex.getMessage());
            Log.getInstance().logErrorln(getClass().getName(), 81, ex.getMessage());
			if (msg != null) { 
				usermap.remove(msg.getAuthor());
			}
		}
	}

	/*
	public void handleParticularResponseRoutine(Message mes) {
		/*if (TypeMatcher.isInstanceOf(mes, GameStartResponse.class) && handler.hasGameStarted()) {
		    startFeedbackThread((GameStartResponse) mes);
		}
	}

    /**
     * A continuous thread which spits out all infromation from the game to the players
     *
	public void startFeedbackThread(GameStartResponse resp) {// send message to all players
        /*gameFeedbackThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // while the game is running
                while (handler.hasGameStarted()) { // ensure that the server is running
                    handleOutputFromGame();
                }
            }
        });
        gameFeedbackThread.start();*/
	//}
    /**
	 * // CLIENT GETS UNPARSED INFO! FIX THIS!
     * // MAYBE THIS WILL BE DONE LATER, idk
     * Announces the output from the game, should anything be recieved
     */
	private void handleOutputFromGame() {
        /*if (ioqueue.containsOutgoingElements()) {
            // recieve message from the game...
            String response = (String) ioqueue.dequeueToExternal();
            //response = response.substring(response.indexOf("|") + 1, response.lastIndexOf("|"));
            Message msg = MessageUtils.constructMessage("GAME", response, MessageType.Game);

            network.announceMessagePublicly(usermap, msg);
            //mainArea.appendText("[GAME]" + announcement.build() + " \n");
            mainArea.appendText(MessageUtils.readableMessageFormatForServer(msg));
        }*/
    }

    /**
     * Given a username, check to see if it is already contained within the
     * map of names.
     * @param username name to check for.
     * @return the name that is not already used by a user.
     */
	private String handleUsernameTakenRoutine(String username) {
	    StringBuilder sb = new StringBuilder(username);
	    while (usermap.containsKey(sb.toString())) {
	        sb.append(".jr");
        }
        return sb.toString();
	}
	/**
	 * Handles the disconnection of a user.
	 * @param usr usr that disconnected
	 * @param msg information about the users disconnection
	 */
	private void handleUserJustDisconnectedAction(User usr, Message msg) {
		usermap.remove(usr.getName());
		network.announceDisconnectionMessage(usermap, msg.getAuthor());
		JavaFXUtils.threadSafeAppendToTextArea(mainArea, usr.getName() + " has disconnected.");
		mainArea.positionCaret(mainArea.getLength());
	}

    /**
     * Given a message signifying that a user just connected, handle all of the
     * utilities for when a user connects to the server.
     * @param msg information about the users connection.
     */
	private void handleUserJustConnectedAction(Message msg) {
		String name = handleUsernameTakenRoutine(msg.getAuthor());
		User usr = new User(name, writer, usermap.isEmpty());
		network.announceUpdateMessage(usr, name);
		network.announceConnectionMessage(usermap, name);
		
		usermap.put(usr.getName(), usr);
		JavaFXUtils.threadSafeAppendToTextArea(mainArea, usr.getName() + " has connected.");
		mainArea.positionCaret(mainArea.getLength());
	}
}