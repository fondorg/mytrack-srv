package ru.fondorg.mytracksrv.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Issue {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
//    @Column(nullable = false)
    private Long pid;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime created;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 2048)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Column(nullable = false)
    private Boolean closed = false;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Tag> tags;

}
