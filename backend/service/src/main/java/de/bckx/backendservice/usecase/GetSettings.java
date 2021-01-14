package de.bckx.backendservice.usecase;

import de.bckx.backendservice.model.LogEntry;
import de.bckx.backendservice.model.LogEntryCategory;
import de.bckx.backendservice.model.LogEntryLevel;
import de.bckx.backendservice.model.Settings;
import de.bckx.backendservice.repository.SettingsRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * GetSettings
 */
@Service
public class GetSettings {

  @Value("${spring.application.name}")
  private String DEFAULT_APPLICATION_NAME;

  private final SettingsRepository settingsRepository;
  private final AddLogEntry addLogEntry;

  public GetSettings(
    SettingsRepository settingsRepository,
    AddLogEntry addLogEntry
  ) {
    this.settingsRepository = settingsRepository;
    this.addLogEntry = addLogEntry;
  }

  public Settings get() {
    Settings settings = new Settings();
    Optional<Settings> optSettings = settingsRepository
      .findAll()
      .stream()
      .findFirst();
    if (optSettings.isPresent()) {
      settings = optSettings.get();
    } else {
      settings.setApplicationName(DEFAULT_APPLICATION_NAME);
      settings = settingsRepository.save(settings);
    }

    addLogEntry.add(
      new LogEntry(
        LogEntryCategory.GET_SETTINGS,
        LogEntryLevel.INFO,
        "Settings retrieved successfully"
      )
    );

    return settings;
  }
}
