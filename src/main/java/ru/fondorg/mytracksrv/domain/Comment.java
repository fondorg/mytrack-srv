package ru.fondorg.mytracksrv.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime created;

    @NotNull
    @Column(name = "text", columnDefinition = "TEXT", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Issue issue;
}
