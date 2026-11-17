import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BrokerServer extends Thread {
    private final int port;
    private final Broker broker;
    private final Displayer<String> displayer;
    private ServerSocket serverSocket;
    private boolean running = true;

    public BrokerServer(int port, Broker broker, Displayer<String> displayer) {
        this.port = port;
        this.broker = broker;
        this.displayer = displayer;
    }

    private void log(String message) {
        if (displayer != null) {
            displayer.display(message);
        } else {
            System.out.println(message);
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            log("Broker Server started on port " + port);

            while (running) {
                // 1. Accept incoming client connection
                Socket socket = serverSocket.accept();

                // 2. Instantiate ClientHandler with socket, shared broker, and logger
                ClientHandler clientHandler = new ClientHandler(socket, broker, displayer);

                // 3. Start dedicated worker thread for the client
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            if (running) {
                log("Server error: " + e.getMessage());
            }
        } finally {
            stopServer();
        }
    }

    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log("Error closing server socket: " + e.getMessage());
        }
    }
}