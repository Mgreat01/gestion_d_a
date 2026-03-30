package cd.mbaka.gestionD.document.service;

import cd.mbaka.gestionD.document.model.DocumentModel;
import cd.mbaka.gestionD.document.persistence.DocumentRepository;
import cd.mbaka.gestionD.user.model.UserModel;
import cd.mbaka.gestionD.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final Path root = Paths.get("uploads");

    public DocumentModel save(DocumentModel document, MultipartFile file) {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Files.copy(file.getInputStream(), this.root.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);

            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserModel user = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

            document.setUser(user);
            document.setCreatedBy(user.getFullName());
            document.setFileName(uniqueFileName);
            document.setFileSize(file.getSize());
            document.setFileType(file.getContentType());

            return documentRepository.save(document);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de stocker le fichier : " + e.getMessage());
        }
    }

    public List<DocumentModel> findAll() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return documentRepository.findByUserEmail(currentUserEmail);
    }

    public void delete(Long id) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        DocumentModel doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));
        if (!doc.getUser().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Vous n'avez pas le droit de supprimer ce document");
        }

        try {
            Path fileToDelete = this.root.resolve(doc.getFileName());

           Files.deleteIfExists(fileToDelete);

            documentRepository.deleteById(id);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la suppression physique du fichier : " + e.getMessage());
        }
    }
}