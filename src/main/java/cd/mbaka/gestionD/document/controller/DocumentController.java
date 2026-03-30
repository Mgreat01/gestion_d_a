package cd.mbaka.gestionD.document.controller;

import cd.mbaka.gestionD.document.model.DocumentModel;
import cd.mbaka.gestionD.document.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DocumentController {

    private final DocumentService service;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<DocumentModel>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentModel> create(
            @RequestParam("document") String documentJson,
            @RequestParam("file") MultipartFile file) {
        try {
            objectMapper.registerModule(new JavaTimeModule());

            DocumentModel document = objectMapper.readValue(documentJson, DocumentModel.class);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.save(document, file));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}