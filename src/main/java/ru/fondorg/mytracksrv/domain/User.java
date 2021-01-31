package ru.fondorg.mytracksrv.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "USER")
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("userReference")
    private Set<ProjectParticipant> participants = new HashSet<>();

    public void addParticipant(ProjectParticipant participant) {
        this.participants.add(participant);
    }

    public void removeParticipant(ProjectParticipant participant) {
        participants.removeIf(pp -> pp.equals(participant));
    }
}
