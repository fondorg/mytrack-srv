package ru.fondorg.mytracksrv.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.fondorg.mytracksrv.domain.ProjectParticipant;
import ru.fondorg.mytracksrv.domain.ProjectParticipantKey;
import ru.fondorg.mytracksrv.domain.User;

import java.util.List;
import java.util.Optional;

public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipant, ProjectParticipantKey> {

    List<ProjectParticipant> findDistinctByUserId(String userId);

    List<ProjectParticipant> findDistinctByProjectId(Long projectId);

    Optional<ProjectParticipant> findByUserIdAndProjectId(String userId, Long projectId);

    void deleteByProjectId(Long projectId);

    Page<ProjectProjection> findDistinctByUserOrderByProjectDesc(User user, Pageable pageable);

}
