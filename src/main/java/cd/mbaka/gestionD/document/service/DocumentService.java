package cd.mbaka.gestionD.document.service;

import cd.mbaka.gestionD.document.model.DocumentModel;
import cd.mbaka.gestionD.document.persistence.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository repository;
    private final String STORAGE_PATH = "uploads/documents/";

    public List<DocumentModel> findAll() {
        return repository.findAll();
    }

    @Transactional
    public DocumentModel save(DocumentModel doc, MultipartFile file) throws IOException {
        Path root = Paths.get(STORAGE_PATH);
        if (!Files.exists(root)) Files.createDirectories(root);

        // Sécurisation du nom de fichier
        String originalName = Optional.ofNullable(file.getOriginalFilename()).orElse("unnamed");
        String cleanFileName = System.currentTimeMillis() + "_" + originalName.replaceAll("\\s+", "_");

        Path targetPath = root.resolve(cleanFileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // gestion de Mise à jour des métadonnées
        doc.setFileName(cleanFileName);
        doc.setFileSize(file.getSize());
        doc.setFileType(file.getContentType());
        doc.setFilePath(targetPath.toString());

        return repository.save(doc);
    }

    @Transactional
    public void delete(Long id) {
        repository.findById(id).ifPresent(doc -> {
            try {
                Files.deleteIfExists(Paths.get(doc.getFilePath()));
            } catch (IOException e) {
                System.err.println("Impossible de supprimer le fichier physique : " + e.getMessage());
            }
            repository.deleteById(id);
        });
    }

    public List<DocumentModel> findMyDocuments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByUserEmail(email);
    }
}