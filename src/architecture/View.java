package architecture;

import dialogs.Constants;
import drawing.CanvasObject;
import drawing.CanvasObjectInterpreter;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import network.IOUHolder;

public class View {
	
	private GraphicsContext context;
	private TextArea chat;
	private CanvasObject[] canvasObjects;
	private CanvasObjectInterpreter coi;

	public View(GraphicsContext context, TextArea chat) {
		this.context = context;
		this.chat = chat;
		coi = new CanvasObjectInterpreter();
	}
	
	public void appendTextToChat(String msg) { chat.appendText(msg + "\n"); }

	public void handleCanvasInput(IOUHolder iouholder) {
		if (iouholder.getCanvasObjects().isEmpty() == false) {
			// retrieve all the canvas objects
			canvasObjects = coi.extractCanvasObjects(iouholder.getCanvasObjects().poll());
			for (int i = 0; i < canvasObjects.length; i++) {
				CanvasObject co = canvasObjects[i];
				// check the input
				if (co.isConsumable()) {
                    Platform.runLater( () -> context.setFill(Paint.valueOf(co.getPoint().getColor())) );
                    Platform.runLater( () -> context.fillText(co.getData(), co.getPoint().getX(), co.getPoint().getY()));
				} else {
					Platform.runLater( () -> context.setFill(Paint.valueOf(co.getPoint().getColor())) );
					Platform.runLater( () -> context.fillOval(co.getPoint().getX(), co.getPoint().getY(), Constants.PEN_SIZE, Constants.PEN_SIZE) );
				}
			}
		}
	}

}
