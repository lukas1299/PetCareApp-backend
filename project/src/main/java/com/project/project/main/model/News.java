package com.project.project.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "news")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class News {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String date;
    private String title;

    @Type(type="org.hibernate.type.BinaryType")
    private byte[] photo;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Paragraph> paragraphList;

    public static News fromDto(String title, byte[] file, String date){
        return News.builder()
                .id(UUID.randomUUID())
                .title(title)
                .date(date)
                .photo(file)
                .build();
    }
}
