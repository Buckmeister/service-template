package de.bckx.backendservice.controller;

import de.bckx.backendservice.api.LogsApi;
import de.bckx.backendservice.dto.LogEntryDTO;
import de.bckx.backendservice.model.LogEntry;
import de.bckx.backendservice.usecase.AddLogEntry;
import de.bckx.backendservice.usecase.GetLogEntries;
import de.bckx.backendservice.usecase.GetLogEntry;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

@Api(tags = { "Logs" })
@RestController
@RequestMapping("${openapi.userManagement.base-path:/api/v1}")
public class LogsApiController implements LogsApi {

  private final NativeWebRequest request;
  private final AddLogEntry addLogEntry;
  private final GetLogEntries getLogEntries;
  private final GetLogEntry getLogEntry;
  private final ModelMapper modelMapper;

  public LogsApiController(
    NativeWebRequest request,
    AddLogEntry addLogEntry,
    GetLogEntries getLogEntries,
    GetLogEntry getLogEntry,
    ModelMapper modelMapper
  ) {
    this.request = request;
    this.addLogEntry = addLogEntry;
    this.getLogEntries = getLogEntries;
    this.getLogEntry = getLogEntry;
    this.modelMapper = modelMapper;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  @Override
  public ResponseEntity<List<LogEntryDTO>> getLogEntries(
    @Valid Integer limit,
    @Valid Integer offset
  ) {
    return new ResponseEntity<List<LogEntryDTO>>(
      getLogEntries
        .get()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList()),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<LogEntryDTO> getLogEntry(UUID entryId) {
    return new ResponseEntity<LogEntryDTO>(
      convertToDTO(getLogEntry.get(entryId)),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<LogEntryDTO> addLogEntry(
    @Valid LogEntryDTO logEntryDTO
  ) {
    LogEntry newLogEntry = convertFromDTO(logEntryDTO);
    LogEntry createdLogEntry = addLogEntry.add(newLogEntry);
    return new ResponseEntity<LogEntryDTO>(
      convertToDTO(createdLogEntry),
      HttpStatus.OK
    );
  }

  private LogEntry convertFromDTO(LogEntryDTO logsEntryDTO) {
    return modelMapper.map(logsEntryDTO, LogEntry.class);
  }

  private LogEntryDTO convertToDTO(LogEntry logEntry) {
    return modelMapper.map(logEntry, LogEntryDTO.class);
  }
}
