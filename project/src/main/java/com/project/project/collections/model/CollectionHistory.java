package com.project.project.collections.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.project.main.model.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "collection_history")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CollectionHistory {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    @JsonIgnore
    private Collection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "money")
    private BigDecimal money;

    public static CollectionHistory fromDto(Collection collection, User user, DonateRequest donateRequest){
        return CollectionHistory.builder()
                .id(UUID.randomUUID())
                .collection(collection)
                .user(user)
                .money(donateRequest.money())
                .build();
    }
}
