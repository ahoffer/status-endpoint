package status;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
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
    JobStatus status = lookupStatus(jobId);
    log.info("Request for {}, responding with {} ", jobId, status.toString());
    if (status.equals(JobStatus.NOTSTARTED)) {
      throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
    }
    return status.toString();
  }

  private JobStatus lookupStatus(String jobId) {
    return jobs.computeIfAbsent(jobId, jobFactory::next).getCurrentStatus();
  }

  @DeleteMapping("/job")
  void reset() {
    log.info("Resetting state of status endpoint");
    jobs = new HashMap<>();
  }
}
