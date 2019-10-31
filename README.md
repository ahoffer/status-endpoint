## Simulated Job Status Endpoint
A endpoint for testing polling

###Instructions
1. Start the Spring application
2. In a browser, go to `http://localhost:9000/job/1`
3. The current status of job #1 is dipslayed in the browser, e.g. **INPROGRESS**
4. Refresh the browser several times until the status changes. Some jobs are stuck in the INPROGRESS and never complete. This is part of the simulation.

### How it works
The endpoint's path is `/job/{id}`, where `id` can be any string. If the string is unrecognized, a new job is created. If `id` is recognized, its current status is returned.

The lifecycle of a job is:
```
                                 +----> COMPLETED
   NOT STARTED +--> IN PROGRESS -|
                                 +----> FAILED
```

* The lifecycle of a `Job` is determined randomly by the `JobFactory` when the `Job` is created.
* The probability of moving from one state to the next is hardcoded in the `JoStateMachine` class.