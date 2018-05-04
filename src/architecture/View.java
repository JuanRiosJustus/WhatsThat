package architecture;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;

public class View {
	
	private GraphicsContext context;
	private TextArea chat;
	
	public View(GraphicsContext context, TextArea chat) {
		this.context = context;
		this.chat = chat;
	}
	
	public void appendTextToChat(String msg) { chat.appendText(msg + "\n"); }
}
