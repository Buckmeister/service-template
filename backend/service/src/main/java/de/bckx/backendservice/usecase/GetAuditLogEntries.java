package de.bckx.backendservice.usecase;

import de.bckx.backendservice.model.AuditLogEntry;
import de.bckx.backendservice.repository.AuditLogEntryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetLogEntries
 */
@Service
public class GetAuditLogEntries {

  private final AuditLogEntryRepository auditLogEntryRepository;

  public GetAuditLogEntries(AuditLogEntryRepository auditLogEntryRepository) {
    this.auditLogEntryRepository = auditLogEntryRepository;
  }

  public List<AuditLogEntry> get() {
    return auditLogEntryRepository.findAll();
  }
}
