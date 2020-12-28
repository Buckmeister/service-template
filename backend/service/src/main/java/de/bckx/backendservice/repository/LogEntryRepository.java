package de.bckx.backendservice.repository;

import de.bckx.backendservice.model.LogEntry;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * LogEntryRepository
 */
@Repository
public interface LogEntryRepository extends MongoRepository<LogEntry, UUID> {}
