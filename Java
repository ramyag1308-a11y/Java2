class SharedBuffer {
    private int item;
    private boolean available = false;

    // Producer puts an item into the buffer
    synchronized void produce(int item) {
        try {
            while (available) {
                wait();
            }

            this.item = item;
            available = true;

            System.out.println("Produced: " + item);

            notify();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    // Consumer takes an item from the buffer
    synchronized void consume() {
        try {
            while (!available) {
                wait();
            }

            System.out.println("Consumed: " + item);
            available = false;

            notify();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}

class Producer extends Thread {
    SharedBuffer buffer;

    Producer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            buffer.produce(i);
        }
    }
}

class Consumer extends Thread {
    SharedBuffer buffer;

    Consumer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            buffer.consume();
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {

        SharedBuffer buffer = new SharedBuffer();

        Producer p = new Producer(buffer);
        Consumer c = new Consumer(buffer);

        p.start();
        c.start();
    }
}
