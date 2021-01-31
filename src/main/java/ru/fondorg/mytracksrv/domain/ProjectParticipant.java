package ru.fondorg.mytracksrv.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

//@Data
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ProjectParticipant {

    @EmbeddedId
    private ProjectParticipantKey id;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    @JsonBackReference("projectReference")
    private Project project;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonBackReference("userReference")
    private User user;

    private ParticipantRole role;

    public ProjectParticipant(Project project, User user, ParticipantRole role) {
        this.id = new ProjectParticipantKey();
        this.project = project;
        this.user = user;
        this.role = role;
    }

    public ProjectParticipant(Project project, User user) {
        this.id = new ProjectParticipantKey();
        this.project = project;
        this.user = user;
    }

    @Override
    public int hashCode() {
        if (project == null || user == null) {
            return super.hashCode();
        }
        int result = 17;
        result = 31 * result + project.getId().hashCode();
        result = 31 * result + user.getId().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == getClass()) {
            ProjectParticipant pp = (ProjectParticipant) obj;
            return project.getId().equals(pp.getProject().getId()) &&
                    user.getId().equals(pp.getUser().getId());
        }
        return false;
    }
}
