package de.bckx.backendservice.controller;

import de.bckx.backendservice.api.SettingsApi;
import de.bckx.backendservice.dto.SettingsDTO;
import de.bckx.backendservice.model.Settings;
import de.bckx.backendservice.usecase.GetSettings;
import de.bckx.backendservice.usecase.SetSettings;
import io.swagger.annotations.Api;
import java.util.Optional;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

@Api(tags = { "Settings" })
@RestController
@RequestMapping("${openapi.serviceTemplate.base-path:/api/v1}")
public class SettingsApiController implements SettingsApi {

  private final GetSettings getSettings;
  private final SetSettings setSettings;
  private final NativeWebRequest request;
  private final ModelMapper modelMapper;

  public SettingsApiController(
    GetSettings getSettings,
    SetSettings setSettings,
    ModelMapper modelMapper,
    NativeWebRequest request
  ) {
    this.getSettings = getSettings;
    this.setSettings = setSettings;
    this.modelMapper = modelMapper;
    this.request = request;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  @Override
  public ResponseEntity<SettingsDTO> setSettings(
    @Valid SettingsDTO settingsDTO
  ) {
    Settings newSettings = convertFromDTO(settingsDTO);
    Settings storedSettings = setSettings.set(newSettings);
    return new ResponseEntity<SettingsDTO>(
      convertToDTO(storedSettings),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<SettingsDTO> getSettings() {
    return new ResponseEntity<SettingsDTO>(
      convertToDTO(getSettings.get()),
      HttpStatus.OK
    );
  }

  private Settings convertFromDTO(SettingsDTO settingsDTO) {
    return modelMapper.map(settingsDTO, Settings.class);
  }

  private SettingsDTO convertToDTO(Settings settings) {
    return modelMapper.map(settings, SettingsDTO.class);
  }
}
