package cd.mbaka.gestionD.document.controller;

import cd.mbaka.gestionD.document.model.DocumentModel;
import cd.mbaka.gestionD.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public List<DocumentModel> getAll() {
        return service.findAll();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<DocumentModel> create(
            @RequestPart("document") DocumentModel document,
            @RequestPart("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(service.save(document, file));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}