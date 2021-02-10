package de.bckx.backendservice.controller;

import de.bckx.backendservice.api.AuditlogsApi;
import de.bckx.backendservice.dto.AuditLogEntryDTO;
import de.bckx.backendservice.model.AuditLogEntry;
import de.bckx.backendservice.usecase.GetAuditLogEntries;
import de.bckx.backendservice.usecase.GetAuditLogEntry;
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

@Api(tags = { "Audit Logs" })
@RestController
@RequestMapping("${openapi.serviceTemplate.base-path:/api/v1}")
public class AuditLogsApiController implements AuditlogsApi {

  private final NativeWebRequest request;
  private final GetAuditLogEntries getAuditLogEntries;
  private final GetAuditLogEntry getAuditLogEntry;
  private final ModelMapper modelMapper;

  public AuditLogsApiController(
    NativeWebRequest request,
    GetAuditLogEntries getAuditLogEntries,
    GetAuditLogEntry getAuditLogEntry,
    ModelMapper modelMapper
  ) {
    this.request = request;
    this.getAuditLogEntries = getAuditLogEntries;
    this.getAuditLogEntry = getAuditLogEntry;
    this.modelMapper = modelMapper;
  }

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  @Override
  public ResponseEntity<List<AuditLogEntryDTO>> getAuditLogEntries(
    @Valid Integer limit,
    @Valid Integer offset
  ) {
    return new ResponseEntity<List<AuditLogEntryDTO>>(
      getAuditLogEntries
        .get()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList()),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<AuditLogEntryDTO> getAuditLogEntry(UUID entryId) {
    return new ResponseEntity<AuditLogEntryDTO>(
      convertToDTO(getAuditLogEntry.get(entryId)),
      HttpStatus.OK
    );
  }

  private AuditLogEntryDTO convertToDTO(AuditLogEntry logEntry) {
    return modelMapper.map(logEntry, AuditLogEntryDTO.class);
  }
}
