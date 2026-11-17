import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable, Subscriber<Object> {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    private final int clientId;
    private final Socket socket;
    private final Broker broker;
    private final Displayer<String> displayer;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ClientHandler(Socket socket, Broker broker, Displayer<String> displayer) {
        this.socket = socket;
        this.broker = broker;
        this.displayer = displayer;
        this.clientId = ID_GENERATOR.getAndIncrement();
    }

    public int getClientId() {
        return clientId;
    }

    private void log(String msg) {
        if (displayer != null) {
            displayer.display("[Client #" + clientId + "] " + msg);
        } else {
            System.out.println("[Client #" + clientId + "] " + msg);
        }
    }

    @Override
    public void run() {
        try {
            // Setup streams
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            // Send unique assigned client ID to the client immediately
            oos.writeObject(clientId);
            oos.flush();

            log("Connected from: " + socket.getInetAddress().getHostAddress());

            while (!socket.isClosed()) {
                Object rawObj = ois.readObject();

                if (rawObj instanceof NetworkCommand) {
                    NetworkCommand cmd = (NetworkCommand) rawObj;

                    switch (cmd.getAction()) {
                        case SUBSCRIBE:
                            if (cmd.getTopic() != null) {
                                broker.subscribe(cmd.getTopic(), this);
                                log("Subscribed to topic: " + cmd.getTopic());
                            }
                            break;

                        case UNSUBSCRIBE:
                            if (cmd.getTopic() != null) {
                                broker.unsubscribe(cmd.getTopic(), this);
                                log("Unsubscribed from topic: " + cmd.getTopic());
                            }
                            break;

                        case PUBLISH:
                            Message<?> msg = cmd.getMessage();
                            if (msg != null) {
                                broker.publish(msg);
                                log("Published message on topic: " + msg.getTopic());
                            }
                            break;
                    }
                }
            }
        } catch (EOFException e) {
            log("Disconnected gracefully.");
        } catch (Exception e) {
            log("Connection error: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public void onMessage(Message<Object> msg) {
        sendToClient(msg);
    }

    public synchronized void sendToClient(Object payload) {
        try {
            oos.writeObject(payload);
            oos.flush();
        } catch (IOException e) {
            log("Error sending data to client: " + e.getMessage());
        }
    }

    private void closeConnection() {
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            log("Error closing socket: " + e.getMessage());
        }
    }
}