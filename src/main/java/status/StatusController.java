package status;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class StatusController {

  Map<String, Job> jobs;
  JobFactory jobFactory;

  public StatusController() {
    jobFactory = new JobFactory();
    reset();
  }

  @GetMapping(value = "/job/{jobId}")
  // Synchronize to avoid concurrent modification exception
  public synchronized String status(@PathVariable String jobId) {
    JobStatus status = jobs.computeIfAbsent(jobId, jobFactory::next).getCurrentStatus();
    if (status.equals(JobStatus.NOTSTARTED)) {
      throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
    }
    return status.toString();
  }

  @DeleteMapping("/all")
  void reset() {
    jobs = new HashMap<>();
  }
}
