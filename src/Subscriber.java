@FunctionalInterface
public interface Subscriber<T> {
    void onMessage(Message<T> msg);
}
