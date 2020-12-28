package de.bckx.backendservice.usecase;

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

  public SetSettings(SettingsRepository settingsRepository) {
    this.settingsRepository = settingsRepository;
  }

  public Settings set(Settings newSettings) {
    Optional<Settings> optExistingSettings = settingsRepository
      .findAll()
      .stream()
      .findFirst();

    if (optExistingSettings.isPresent()) {
      Settings existingSettings = optExistingSettings.get();
      newSettings.setId(existingSettings.getId());
    }
    return settingsRepository.save(newSettings);
  }
}
