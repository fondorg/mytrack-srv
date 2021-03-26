package ru.fondorg.mytracksrv.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.domain.Tag;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    Page<Issue> findByProjectId(Long projectId, Pageable pageable);

    Page<Issue> findByProjectIdAndClosed(Long projectId, Boolean closed, Pageable pageable);

    Long countByProjectId(Long projectId);

    void deleteByProjectId(Long projectId);

    @Query("select i.tags from Issue i where i.id = :#{#issueId}")
    List<Tag> findIssueTags(@Param("issueId") Long issueId);
}
