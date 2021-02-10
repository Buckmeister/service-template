package de.bckx.backendservice.usecase;

import de.bckx.backendservice.model.AuditLogEntry;
import de.bckx.backendservice.model.AuditLogEntryCategory;
import de.bckx.backendservice.model.AuditLogEntryLevel;
import de.bckx.backendservice.model.Settings;
import de.bckx.backendservice.repository.SettingsRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * SetSettings
 */
@Service
public class SetSettings {

  private final SettingsRepository settingsRepository;
  private final AddAuditLogEntry addLogEntry;

  public SetSettings(
    SettingsRepository settingsRepository,
    AddAuditLogEntry addLogEntry
  ) {
    this.settingsRepository = settingsRepository;
    this.addLogEntry = addLogEntry;
  }

  public Settings set(Settings newSettings) {
    Optional<Settings> optExistingSettings = settingsRepository.findFirstByOrderByIdAsc();
    if (optExistingSettings.isPresent()) {
      Settings existingSettings = optExistingSettings.get();
      newSettings.setId(existingSettings.getId());
    }

    addLogEntry.add(
      new AuditLogEntry(
        AuditLogEntryCategory.SET_SETTINGS,
        AuditLogEntryLevel.INFO,
        "Application Name: " + newSettings.getApplicationName()
      )
    );

    return settingsRepository.save(newSettings);
  }
}
