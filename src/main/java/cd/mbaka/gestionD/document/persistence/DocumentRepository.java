package cd.mbaka.gestionD.document.persistence;

import cd.mbaka.gestionD.document.model.DocumentModel;
import cd.mbaka.gestionD.document.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DocumentRepository extends JpaRepository<DocumentModel, Long> {
    long countByType(DocumentType type);

    long countByIsFavoriteTrue();

    List<DocumentModel> findByUserEmail(String email);
}