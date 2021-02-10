package de.bckx.backendservice.model;

import java.util.UUID;

/**
 * LogEntry
 */
public class AuditLogEntry {

  UUID id = UUID.randomUUID();

  AuditLogEntryCategory category;
  AuditLogEntryLevel level;
  String message;

  public AuditLogEntry() {}

  public AuditLogEntry(
    AuditLogEntryCategory category,
    AuditLogEntryLevel level,
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

  public AuditLogEntryCategory getCategory() {
    return this.category;
  }

  public void setCategory(AuditLogEntryCategory category) {
    this.category = category;
  }

  public AuditLogEntryLevel getLevel() {
    return this.level;
  }

  public void setLevel(AuditLogEntryLevel level) {
    this.level = level;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
