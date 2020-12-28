package de.bckx.backendservice.usecase;

import de.bckx.backendservice.model.LogEntry;
import de.bckx.backendservice.repository.LogEntryRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * GetLogEntry
 */
@Service
public class GetLogEntry {

  private final LogEntryRepository logEntryRepository;

  public GetLogEntry(LogEntryRepository logEntryRepository) {
    this.logEntryRepository = logEntryRepository;
  }

  public LogEntry get(UUID id) {
    LogEntry logEntry = new LogEntry();
    Optional<LogEntry> optLogEntry = logEntryRepository.findById(id);
    if (optLogEntry.isPresent()) {
      logEntry = optLogEntry.get();
    }
    return logEntry;
  }
}
