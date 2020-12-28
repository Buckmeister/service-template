package de.bckx.backendservice.usecase;

import de.bckx.backendservice.model.LogEntry;
import de.bckx.backendservice.repository.LogEntryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetLogEntries
 */
@Service
public class GetLogEntries {

  private final LogEntryRepository logEntryRepository;

  public GetLogEntries(LogEntryRepository logEntryRepository) {
    this.logEntryRepository = logEntryRepository;
  }

  public List<LogEntry> get() {
    return logEntryRepository.findAll();
  }
}
