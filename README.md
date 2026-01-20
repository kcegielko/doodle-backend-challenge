# doodle-backend-challenge
Doodle Backend Challenge

## Steps:

### 1) Core Algorithm

To efficiently find all **common free time slots** among multiple participants, the solution uses the **Sweep Line Algorithm** (also known as the line sweep or events sorting technique).

### Why Sweep Line?

- Classic and well-understood approach for interval problems
- Clean O(n log n) complexity after sorting events
- Highly reusable and extensible for variations of the problem
- Easy to read, debug, and maintain

Implementation:  
`SweepLineAlgorithm.java`

Dedicated unit tests for the algorithm itself:  
`SweepLineAlgorithmTest.java`