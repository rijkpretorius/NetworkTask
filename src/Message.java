import java.io.Serializable;

public final class Message<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String topic;
    private final int publisher;
    private final T data;

    public Message(String topic, int publisher, T data) {
        this.topic = topic;
        this.publisher = publisher;
        this.data = data;
    }

    public String getTopic() { return topic; }
    public int getPublisher() { return publisher; }
    public T getData() { return data; }

    @Override
    public String toString() {
        return String.format("Message[topic='%s', publisher=%s, data=%s]", topic, publisher, data);
    }
}
