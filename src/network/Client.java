package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import dialogs.Constants;
import drawing.CanvasObjectInterpreter;
import javafx.scene.control.TextArea;
import messaging.Message;
import messaging.MessageUtils;
import messaging.MessageType;
import utlility.JavaFXUtils;

public class Client extends NetworkNode implements Runnable {

    private CanvasObjectInterpreter pi;
	private TextArea chatArea;
	private Network network;
	private String username;
	
	public Client(TextArea ta, IOUHolder ios, BufferedReader br, String name) {
		super(br, ios);
		chatArea = ta;
		username = name;
		pi = new CanvasObjectInterpreter();
		network = new Network();
	}

    /**
     * Handles the given input stream from the server.
     */
	public void run() {
		String stream = null;
		Message msg = null;
		
		try {
			while ((stream = input.readLine()) != null) {
				if (MessageUtils.streamContainsInsufficientDelimiters(stream)) { continue; }
				msg = MessageUtils.parseMessageBuild(stream);
				MessageType type = MessageUtils.getMessageType(msg.getMessageTypeValueAsString());
				switch (type) {
					case Connect: {
						usermap.put(msg.getAuthor(), new User(msg.getAuthor()));
                        JavaFXUtils.threadSafeAppendToTextArea(chatArea, msg.getAuthor() + " has connected.");
					} break;
					case Disconnect: {
						usermap.remove(msg.getAuthor());
						JavaFXUtils.threadSafeAppendToTextArea(chatArea, msg.getAuthor() + " has disconnected.");
					} break;
					case Public: {
                        JavaFXUtils.threadSafeAppendToTextAreaForClient(chatArea, msg);
					} break;
                    case Command: {
                        JavaFXUtils.threadSafeAppendToTextAreaForClient(chatArea, msg);
					} break;
					case Update: {
						ioqueue.enqueueFromExternal(msg.getContent());
					} break;
					case Game: {
                        JavaFXUtils.threadSafeAppendToTextAreaForClient(chatArea, msg);
					} break;
                    case Draw: {
                        canvasObjects.add(msg.getContent());
                    } break;
					default: {
						// TODO make sure to do this later... // THIS COMMAND IS FOR GAME OPTIONS AS WELL
                        JavaFXUtils.threadSafeAppendToTextArea(chatArea, stream);
					} break;
				}
                chatArea.positionCaret(chatArea.getText().length());
			}
		} catch (IOException ex) {
		    JavaFXUtils.threadSafeAppendToTextArea(chatArea, Constants.ERROR_UNABLE_TOCONNECT);
            JavaFXUtils.threadSafeAppendToTextArea(chatArea, ex.getMessage());
		}
	}

    /**
     * Given a message as a string, needs to send a message to the server,
     * using the given stream object.
     * @param writer stream to send the message to.
     * @param msg message to send to the stream.
     * @param name name of the sender
     * @return
     */
	public boolean sendStringToNetwork(PrintWriter writer, String msg, String name) {
	    if (msg.length() < 1) { return false; }
	    return network.sendStringToServer(writer, name, msg);
    }

    /**
     * Given a string which represents a canvasObjects needs to send a message to the server,
     * using the given stream object.
     * @param writer stream to send the message to.
     * @param data canvasObjects to send to the stream.
     * @param name name of the sender
     * @return
     */
    public void sendCanvasObjectsToNetwork(PrintWriter writer, String data, String name) {
		if (data.length() < 1) { return; }
		network.sendCanvasObjectsToServer(writer, data, name);
	}
}
