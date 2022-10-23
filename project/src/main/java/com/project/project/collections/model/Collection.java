package com.project.project.collections.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.project.main.model.Profile;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "collections")
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Collection {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "target")
    private BigDecimal target;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private String creationDate;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CollectionHistory> collections;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonIgnore
    private Profile profile;

    public static Collection fromDto(Profile profile, CollectionRequest collectionRequest, String date) {
        return Collection.builder()
                .id(UUID.randomUUID())
                .profile(profile)
                .collections(new ArrayList<>())
                .title(collectionRequest.title())
                .target(collectionRequest.target())
                .description(collectionRequest.description())
                .creationDate(date)
                .build();
    }
}
