package cd.mbaka.gestionD.document.persistence;

import cd.mbaka.gestionD.document.model.DocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentModel, Long> {
}