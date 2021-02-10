package de.bckx.backendservice.repository;

import de.bckx.backendservice.model.AuditLogEntry;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * LogEntryRepository
 */
@Repository
public interface AuditLogEntryRepository
  extends MongoRepository<AuditLogEntry, UUID> {}
