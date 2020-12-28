package de.bckx.backendservice.usecase;

import de.bckx.backendservice.model.Settings;
import de.bckx.backendservice.repository.SettingsRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * GetSettings
 */
@Service
public class GetSettings {

  @Value("${settings.initial.application.name}")
  private String DEFAULT_APPLICATION_NAME;

  private final SettingsRepository settingsRepository;

  public GetSettings(SettingsRepository settingsRepository) {
    this.settingsRepository = settingsRepository;
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
    return settings;
  }
}
