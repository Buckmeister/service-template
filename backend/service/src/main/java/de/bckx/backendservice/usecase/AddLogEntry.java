package de.bckx.backendservice.usecase;

import de.bckx.backendservice.model.LogEntry;
import de.bckx.backendservice.repository.LogEntryRepository;
import org.springframework.stereotype.Service;

/**
 * AddLogEntry
 */
@Service
public class AddLogEntry {

  private final LogEntryRepository logEntryRepository;

  public AddLogEntry(LogEntryRepository logEntryRepository) {
    this.logEntryRepository = logEntryRepository;
  }

  public LogEntry add(LogEntry logEntry) {
    return logEntryRepository.save(logEntry);
  }
}
