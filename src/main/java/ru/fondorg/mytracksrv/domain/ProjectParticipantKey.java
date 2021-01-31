package ru.fondorg.mytracksrv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

/*@Getter
@Setter*/
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ProjectParticipantKey implements Serializable {

    //@Column(name = "user_id")
    private String userId;

    //@Column(name = "project_id")
    private Long projectId;

    /*@Override
    public boolean equals(Object obj) {
        if (obj.getClass() == getClass()) {
            ProjectParticipantKey other = (ProjectParticipantKey) obj;
            return this.userId.equals(other.getUserId()) && projectId.equals(other.getProjectId());
        }
        return false;
    }*/
}
