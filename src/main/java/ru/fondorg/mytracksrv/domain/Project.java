package ru.fondorg.mytracksrv.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false)
    private java.lang.Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = true)
    private String description;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("projectReference")
    private Set<ProjectParticipant> participants = new HashSet<>();

    @Column
    private String owner;

    public void addParticipant(ProjectParticipant participant) {
        this.participants.add(participant);
    }

    public void removeParticipant(ProjectParticipant participant) {
        /*participants.removeIf(pp -> pp.getId().getProjectId().equals(participant.getId().getProjectId()) &&
                pp.getId().getUserId().equals(participant.getId().getUserId()));*/
        participants.removeIf(pp -> pp.equals(participant));
    }
}
