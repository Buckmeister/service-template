package de.bckx.backendservice.repository;

import de.bckx.backendservice.model.Settings;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * SettingsRepository
 */
@Repository
public interface SettingsRepository extends MongoRepository<Settings, UUID> {}
