package ru.fondorg.mytracksrv.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
public class Tag {

    public Tag(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Tag(String name, String color, Project project) {
        this.name = name;
        this.color = color;
        this.project = project;
    }

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "color", length = 7)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
}
