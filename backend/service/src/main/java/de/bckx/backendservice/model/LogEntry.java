package de.bckx.backendservice.model;

import java.util.UUID;

/**
 * LogEntry
 */
public class LogEntry {

  UUID id = UUID.randomUUID();

  LogEntryCategory category;
  LogEntryLevel level;
  String message;

  public LogEntry() {}

  public LogEntry(
    LogEntryCategory category,
    LogEntryLevel level,
    String message
  ) {
    this.category = category;
    this.level = level;
    this.message = message;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public LogEntryCategory getCategory() {
    return this.category;
  }

  public void setCategory(LogEntryCategory category) {
    this.category = category;
  }

  public LogEntryLevel getLevel() {
    return this.level;
  }

  public void setLevel(LogEntryLevel level) {
    this.level = level;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
