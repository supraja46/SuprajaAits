import java.util.ArrayList;
public class SimpleQueue {
    private ArrayList<Integer> queue;
    public SimpleQueue() {
        queue = new ArrayList<>();
    }
    public void enqueue(int value) {
        queue.add(value);
    }
    public int dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty!");
            return -1;
        }
        return queue.remove(0);
    }
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    public int size() {
        return queue.size();
    }
    public int peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty!");
            return -1;
        }
        return queue.get(0);
    }
    public static void main(String[] args) {
        SimpleQueue q = new SimpleQueue();
        q.enqueue(10);
        q.enqueue(20);
        q.enqueue(30);

        System.out.println("Front element: " + q.peek());
        System.out.println("Dequeued: " + q.dequeue());
        System.out.println("Dequeued: " + q.dequeue());
        System.out.println("Queue size: " + q.size());
    }
}