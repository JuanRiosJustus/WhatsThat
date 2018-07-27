package messaging;

public class Request extends Message {

    public Request(String author, String content, int type) {
        super(author, content, type);
    }
}
