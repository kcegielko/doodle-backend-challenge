# Doodle Backend Challenge

## Approach & Focus

In this solution I deliberately focused **primarily on business logic, core algorithms, and correctness of the time-slot overlapping / availability mechanics** — rather than on persistence layer (database storage), REST API endpoints, input validation, authentication, or full Spring Boot configuration.

I considered typical CRUD operations, REST controllers, DTO ↔ entity mapping, HTTP exception handling, and database setup to be **very standard and repetitive** elements in most backend challenges.  
Far more valuable here is demonstrating a solid understanding of the problem domain, choosing and implementing an appropriate algorithm, and writing clean, well-tested, maintainable code that solves the heart of the task.

**All essential application behaviors are demonstrated and verified through unit and integration tests** — these tests serve as the best living documentation of how the system actually works.

## Use cases:

`SchedulingServiceTest.java` is the file that shows use cases for the solution.

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

### 3) Scheduling Service & User-level Logic
Service responsible for user-specific availability calculations, meetings subtraction and multi-user overlapping logic.

Main class:  
`SchedulingService.java`

Dedicated unit & integration tests covering main scenarios:  
`SchedulingServiceTest.java`

# Running:
***docker compose up --build***