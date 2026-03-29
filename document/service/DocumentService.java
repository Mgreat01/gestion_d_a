package cd.mbaka.gestionD.document.service;

import cd.mbaka.gestionD.document.model.DocumentModel;
import cd.mbaka.gestionD.document.persistence.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository repository;
    private final String STORAGE_PATH = "uploads/documents/";

    public List<DocumentModel> findAll() {
        return repository.findAll();
    }

    public DocumentModel save(DocumentModel doc, MultipartFile file) throws IOException {
        // Création du dossier si inexistant
        Path root = Paths.get(STORAGE_PATH);
        if (!Files.exists(root)) Files.createDirectories(root);

        // Sauvegarde physique
        String cleanFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), root.resolve(cleanFileName), StandardCopyOption.REPLACE_EXISTING);

        // Mise à jour de l'objet
        doc.setFileName(cleanFileName);
        doc.setFileSize(file.getSize());
        doc.setFileType(file.getContentType());
        doc.setFilePath(STORAGE_PATH + cleanFileName);

        return repository.save(doc);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}