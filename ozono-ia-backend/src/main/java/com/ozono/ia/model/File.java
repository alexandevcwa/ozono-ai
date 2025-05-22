package com.ozono.ia.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "file_path", nullable = false, length = 200)
    private String filePath;

    @Column(name = "file_name", nullable = false, length = 75)
    private String fileName;

    @Column(name = "file_type", nullable = false, length = 75)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_url", nullable = false, length = 200)
    private String fileUrl;

    @Column(name = "file_uuid", nullable = false, length = 200)
    private String fileUuid;

    @Column(name = "file_ext", nullable = false, length = 50)
    private String fileExtension;

    @Column(name = "owner", nullable = false)
    private Integer owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", nullable = false, insertable = false, updatable = false)
    private User user;
}