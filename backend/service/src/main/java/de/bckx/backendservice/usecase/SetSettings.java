package de.bckx.backendservice.usecase;

import de.bckx.backendservice.model.LogEntry;
import de.bckx.backendservice.model.LogEntryCategory;
import de.bckx.backendservice.model.LogEntryLevel;
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
  private final AddLogEntry addLogEntry;

  public SetSettings(
    SettingsRepository settingsRepository,
    AddLogEntry addLogEntry
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
      new LogEntry(
        LogEntryCategory.SET_SETTINGS,
        LogEntryLevel.INFO,
        "Application Name: " + newSettings.getApplicationName()
      )
    );

    return settingsRepository.save(newSettings);
  }
}
