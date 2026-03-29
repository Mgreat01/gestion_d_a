package cd.mbaka.gestionD.document.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status = DocumentStatus.actif;

    @Enumerated(EnumType.STRING)
    private DocumentPriority priority;

    private LocalDateTime date;
    private LocalDateTime expirationDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String fileName;
    private long fileSize;
    private String fileType;
    private String filePath;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    private String category;
    private boolean isFavorite = false;
    private boolean isShared = false;

    @ElementCollection
    private List<String> sharedWith = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private List<CommentModel> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private List<DocumentVersionModel> versions = new ArrayList<>();

    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.date == null) this.date = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}