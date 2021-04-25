package ru.fondorg.mytracksrv.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.fondorg.mytracksrv.domain.Comment;
import ru.fondorg.mytracksrv.domain.Issue;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Issue> {
    Page<Comment> findByIssueId(Long issueId, Pageable pageable);
}
