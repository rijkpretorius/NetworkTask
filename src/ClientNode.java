import java.io.*;
import java.net.Socket;

public class ClientNode {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private int clientPublisherId;

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        ois = new ObjectInputStream(socket.getInputStream());

        try {
            this.clientPublisherId = (Integer) ois.readObject();
            System.out.println("Connected to Broker. Assigned Client ID: " + clientPublisherId);
        } catch (ClassNotFoundException e) {
            throw new IOException("Failed to receive client ID from server", e);
        }


        new Thread(this::listenForIncomingMessages).start();
    }

    private void listenForIncomingMessages() {
        try {
            while (!socket.isClosed()) {
                Object received = ois.readObject();
                if (received instanceof Message) {
                    Message<?> msg = (Message<?>) received;
                    System.out.println("RECEIVED: " + msg);
                }
            }
        } catch (Exception e) {
            System.out.println("Connection closed.");
        }
    }

    public void subscribe(String topic) throws IOException {
        oos.writeObject(new NetworkCommand(NetworkCommand.Action.SUBSCRIBE, topic));
        oos.flush();
    }

    public void unsubscribe(String topic) throws IOException {
        oos.writeObject(new NetworkCommand(NetworkCommand.Action.UNSUBSCRIBE, topic));
        oos.flush();
    }

    public <T> void publish(String topic, T content) throws IOException {
        // Uses the server-assigned clientPublisherId as required by the assignment specs
        Message<T> msg = new Message<>(topic, this.clientPublisherId, content);
        oos.writeObject(new NetworkCommand(NetworkCommand.Action.PUBLISH, msg));
        oos.flush();
    }

    public int getClientPublisherId() {
        return clientPublisherId;
    }
}
