package cd.mbaka.gestionD.document.service;

import cd.mbaka.gestionD.document.model.DocumentModel;
import cd.mbaka.gestionD.document.persistence.DocumentRepository;
import cd.mbaka.gestionD.user.model.UserModel;
import cd.mbaka.gestionD.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public DocumentModel save(DocumentModel document, MultipartFile file) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        UserModel user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non connecté ou introuvable"));

        document.setUser(user);
        document.setCreatedBy(user.getFullName());

        document.setFileName(file.getOriginalFilename());
        document.setFileSize(file.getSize());
        document.setFileType(file.getContentType());

        return documentRepository.save(document);
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

        documentRepository.deleteById(id);
    }
}