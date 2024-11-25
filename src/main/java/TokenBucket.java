import java.util.concurrent.TimeUnit;

public class TokenBucket {
    private final int capacity; // Maximum number of tokens in the bucket
    private final int refillRate; // Tokens added per second
    private int currentTokens; // Current number of tokens in the bucket
    private long lastRefillTime; // Timestamp of the last token refill

    public TokenBucket(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.currentTokens = capacity; // Start with a full bucket
        this.lastRefillTime = System.nanoTime();
    }

    // Method to allow a request
    public synchronized boolean allowRequest(int tokensNeeded) {
        refillTokens(); // Refill tokens based on elapsed time

        if (currentTokens >= tokensNeeded) {
            currentTokens -= tokensNeeded; // Consume tokens for the request
            return true; // Allow the request
        } else {
            return false; // Deny the request (not enough tokens)
        }
    }

    // Refill tokens based on elapsed time
    private void refillTokens() {
        long currentTime = System.nanoTime();
        long elapsedTime = currentTime - lastRefillTime;

        // Calculate how many tokens to add based on the elapsed time and refill rate
        int tokensToAdd = (int) (TimeUnit.NANOSECONDS.toSeconds(elapsedTime) * refillRate);

        if (tokensToAdd > 0) {
            currentTokens = Math.min(capacity, currentTokens + tokensToAdd); // Don't exceed bucket capacity
            lastRefillTime = currentTime; // Update the last refill time
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Bucket with capacity of 5 tokens and refill rate of 2 tokens per second
        TokenBucket bucket = new TokenBucket(5, 2);

        System.out.println("Simulating requests...");

        for (int i = 1; i <= 10; i++) {
            if (bucket.allowRequest(1)) { // Each request needs 1 token
                System.out.println("Request " + i + " allowed.");
            } else {
                System.out.println("Request " + i + " denied.");
            }
            Thread.sleep(300); // 300ms between requests
        }

        // Wait for 3 seconds to allow token refill
        System.out.println("\nWaiting for 3 seconds to refill tokens...\n");
        Thread.sleep(3000);

        for (int i = 11; i <= 15; i++) {
            if (bucket.allowRequest(1)) { // Each request needs 1 token
                System.out.println("Request " + i + " allowed.");
            } else {
                System.out.println("Request " + i + " denied.");
            }
            Thread.sleep(300); // 300ms between requests
        }
    }
}
