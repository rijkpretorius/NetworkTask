public class ServerConsole {
    public static void main(String[] args) {
        Broker broker = new Broker();

        Displayer<String> consoleLogger = msg -> System.out.println("[SERVER LOG] " + msg);

        BrokerServer server = new BrokerServer(5001, broker, consoleLogger);
        server.start();

        System.out.println("Broker Server launched successfully.");
    }
}
