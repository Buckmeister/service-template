package de.bckx.backendservice.usecase;

import de.bckx.backendservice.model.AuditLogEntry;
import de.bckx.backendservice.repository.AuditLogEntryRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * GetLogEntry
 */
@Service
public class GetAuditLogEntry {

  private final AuditLogEntryRepository auditLogEntryRepository;

  public GetAuditLogEntry(AuditLogEntryRepository auditLogEntryRepository) {
    this.auditLogEntryRepository = auditLogEntryRepository;
  }

  public AuditLogEntry get(UUID id) {
    AuditLogEntry auditLogEntry = new AuditLogEntry();
    Optional<AuditLogEntry> optLogEntry = auditLogEntryRepository.findById(id);
    if (optLogEntry.isPresent()) {
      auditLogEntry = optLogEntry.get();
    }
    return auditLogEntry;
  }
}
