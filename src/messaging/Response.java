package messaging;

public class Response extends Message implements ErrorContext {

    private boolean error;
    private String descrption;

    public Response(String content, boolean error) {
        super("SERVER", content, MessageType.Public.getValueAsInt());
    }

    @Override
    public boolean getError() {
        return error;
    }
    @Override
    public String getDescription() {
        return getContent();
    }
}
