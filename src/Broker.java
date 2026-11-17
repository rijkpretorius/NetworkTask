import java.util.*;
import java.util.concurrent.*;

public class Broker {
    private final Map<String, List<Subscriber<?>>> subscribers = new ConcurrentHashMap<>();

    public void subscribe(String topic, Subscriber<?> subscriber) {
        subscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(subscriber);
    }

    public void unsubscribe(String topic, Subscriber<?> subscriber) {
        List<Subscriber<?>> list = subscribers.get(topic);
        if (list != null) {
            list.remove(subscriber);
            if (list.isEmpty()) {
                subscribers.remove(topic);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void publish(Message<T> message) {
        String topic = message.getTopic();
        List<Subscriber<?>> list = subscribers.get(topic);

        if (list == null || list.isEmpty()) {
            System.out.println("No subscribers for topic: " + topic);
            return;
        }

        for (Subscriber<?> subscriber : list) {
            ((Subscriber<T>) subscriber).onMessage(message);
        }
    }
}