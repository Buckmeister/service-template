package de.bckx.backendservice.usecase;

import de.bckx.backendservice.model.AuditLogEntry;
import de.bckx.backendservice.repository.AuditLogEntryRepository;
import org.springframework.stereotype.Service;

/**
 * AddLogEntry
 */
@Service
public class AddAuditLogEntry {

  private final AuditLogEntryRepository auditLogEntryRepository;

  public AddAuditLogEntry(AuditLogEntryRepository auditLogEntryRepository) {
    this.auditLogEntryRepository = auditLogEntryRepository;
  }

  public AuditLogEntry add(AuditLogEntry auditLogEntry) {
    return auditLogEntryRepository.save(auditLogEntry);
  }
}
