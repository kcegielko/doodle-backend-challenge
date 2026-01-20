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

### 2) Facade Layer
Higher-level interface that orchestrates the algorithm and provides convenient methods for the application.

Main entry point:  
`OverlappingFacade.java`

Key method added:
- `getTimeSlotsWithoutMeetings(...)`  
  → returns all time slots that are completely free

Implementation details:
- Accepts list of users time slots with their meetings
- Uses the sweep-line algorithm under the hood
- Returns sorted list of free intervals (start → end)

Dedicated integration / facade tests:  
`OverlappingFacadeTest.java`