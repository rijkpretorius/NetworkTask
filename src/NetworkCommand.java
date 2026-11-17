import java.io.Serializable;

public class NetworkCommand implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Action {
        SUBSCRIBE,
        UNSUBSCRIBE,
        PUBLISH
    }

    private final Action action;
    private final String topic;
    private final Message<?> message;

    // Constructor for SUBSCRIBE / UNSUBSCRIBE
    public NetworkCommand(Action action, String topic) {
        this.action = action;
        this.topic = topic;
        this.message = null;
    }

    // Constructor for PUBLISH
    public NetworkCommand(Action action, Message<?> message) {
        this.action = action;
        this.message = message;
        this.topic = (message != null) ? message.getTopic() : null;
    }

    public Action getAction() {
        return action;
    }

    public String getTopic() {
        return topic;
    }

    public Message<?> getMessage() {
        return message;
    }
}
