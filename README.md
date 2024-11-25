# Rate limit algorithm

## Token Bucket

The Token Bucket Algorithm is a rate-limiting algorithm used in networking and APIs to control the rate of requests while allowing occasional bursts. Unlike the Leaky Bucket algorithm, which enforces a constant rate, the Token Bucket algorithm allows a certain number of tokens to accumulate over time, enabling short bursts as long as the bucket has sufficient tokens.

### Concept

The Token Bucket Algorithm is a mechanism used for rate limiting in systems to ensure controlled access to resources. It allows a system to handle requests at a steady rate while accommodating short bursts of activity.

* Token Generation: Tokens are added to the bucket at a fixed rate.
* Token Consumption: A request consumes tokens from the bucket. If there are enough tokens, the request is allowed; otherwise, it is denied.
* Burst Handling: Tokens can accumulate in the bucket up to a maximum capacity, enabling occasional bursts of traffic as long as tokens are available.
* Steady-State Rate: After the bucket is emptied, requests are allowed at a consistent rate matching the token generation rate.

### Steps of the Algorithm

* Initialize the Bucket:
  * Define the bucket's capacity (maximum number of tokens) and the token generation rate (tokens added per second).
* Refill Tokens:
  * Periodically, tokens are added to the bucket at the specified rate, ensuring the total number of tokens does not exceed the bucket's capacity.
* Handle Requests:
  * When a request is received, check if the bucket has enough tokens:
    * If sufficient tokens are available:
      * Consume the required number of tokens and allow the request.
    * If insufficient tokens are available:
      * Deny the request.
* Handle Bursts:
  * If the system was idle and the bucket has accumulated tokens, it allows a burst of requests up to the bucket's capacity.
* Rate Control:
  * Once the bucket is empty, the algorithm allows requests only at the token generation rate.

## Leaky Bucket Algorithm 

The Leaky Bucket Algorithm is a rate-limiting algorithm widely used in networking and application programming to control data flow and ensure stability in systems. It’s named for its resemblance to the way a bucket leaks water at a constant rate.

### Concept
Imagine a bucket with a small hole at the bottom:

Incoming Data: Packets (or requests) are added to the bucket at varying rates.
Fixed Outflow: The bucket leaks (processes requests) at a fixed rate.
Overflow Handling: If the bucket overflows (too many requests), the excess is discarded.
This mechanism ensures that data is processed at a steady rate, regardless of the burstiness of incoming requests.

### Steps of the Algorithm

* Requests (or packets) arrive at the system.
* If the bucket (queue) is not full, the request is added.
* Requests are processed (removed) from the bucket at a constant rate.
* If the bucket overflows (exceeds capacity), excess requests are rejected or dropped.

