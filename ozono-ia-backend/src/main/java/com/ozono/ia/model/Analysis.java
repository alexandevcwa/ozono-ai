package com.ozono.ia.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "analysis")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Analysis {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private File image;

    @Column(name = "photo_id", nullable = false)
    private Long photoId;

    @Column(name = "material_type", length = 50)
    private String material;

    @Column(name = "material_description", length = 250)
    private String description;

    @Column(name = "difficulty_of_recycle", length = 50)
    private String difficulty;

    @Column(name = "disintegration_time", length = 50)
    private String disintegration;

    @Column(name = "contamination_level", length = 150)
    private String contaminationLevel;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
