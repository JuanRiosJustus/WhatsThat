package architecture;

public class Metadata {

    private String username;
    private String endpoint;
    private int port;

    public Metadata(String name, String address, int prt) {
        username = name;
        endpoint = address;
        port = prt;
    }

    public int getPort() { return port; }
    public String getEndIP() { return endpoint; }
    public String getName() { return username; }

}
