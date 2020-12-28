package de.bckx.backendservice.usecase;

import de.bckx.backendservice.model.LogEntry;
import de.bckx.backendservice.repository.LogEntryRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AddLogEntry
 */
@Service
public class AddLogEntry {

  private final LogEntryRepository logEntryRepository;

  @Autowired
  public AddLogEntry(LogEntryRepository logEntryRepository) {
    this.logEntryRepository = logEntryRepository;
  }

  public LogEntry add(LogEntry logEntry) {
    if (logEntry.getId() == null) logEntry.setId(UUID.randomUUID());
    return logEntryRepository.save(logEntry);
  }
}
