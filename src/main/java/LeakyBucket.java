import java.util.concurrent.TimeUnit;

public class LeakyBucket {
    private final int capacity; // Maximum number of tokens the bucket can hold
    private final int leakRate; // Rate at which tokens are leaked (per second)
    private int currentTokens; // Current number of tokens in the bucket
    private long lastLeakTime; // Timestamp of the last leak

    public LeakyBucket(int capacity, int leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.currentTokens = 0;
        this.lastLeakTime = System.nanoTime(); // Initialize to current time
    }

    // Adds tokens to the bucket (e.g., when a new request comes in)
    public synchronized boolean allowRequest() {
        // Leak tokens based on the elapsed time
        leakTokens();

        if (currentTokens < capacity) {
            currentTokens++; // Add a token for the current request
            return true; // Allow the request
        } else {
            return false; // Reject the request (bucket is full)
        }
    }

    // Leaks tokens based on the time elapsed
    private void leakTokens() {
        long currentTime = System.nanoTime();
        long elapsedTime = currentTime - lastLeakTime;

        // Calculate how many tokens to leak based on the elapsed time and leak rate
        int tokensToLeak = (int) (TimeUnit.NANOSECONDS.toSeconds(elapsedTime) * leakRate);
        if (tokensToLeak > 0) {
            currentTokens = Math.max(0, currentTokens - tokensToLeak); // Ensure tokens don't go negative
            lastLeakTime = currentTime; // Update the last leak time
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LeakyBucket bucket = new LeakyBucket(3, 1);

        System.out.println("Simulating burst requests...");
        // Simulate burst requests that exceed capacity
        for (int i = 1; i <= 7; i++) {
            if (bucket.allowRequest()) {
                System.out.println("Request " + i + " allowed.");
            } else {
                System.out.println("Request " + i + " denied.");
            }
            Thread.sleep(300); // 300ms between requests
        }

        // Wait for 5 seconds to allow token leakage
        System.out.println("\nWaiting for 5 seconds to allow tokens to leak...\n");
        Thread.sleep(5000);

        System.out.println("Simulating more requests after waiting...");
        // Simulate more requests after waiting
        for (int i = 7; i <= 20; i++) {
            if (bucket.allowRequest()) {
                System.out.println("Request " + i + " allowed.");
            } else {
                System.out.println("Request " + i + " denied.");
            }
            Thread.sleep(300); // 300ms between requests
        }
    }
}
