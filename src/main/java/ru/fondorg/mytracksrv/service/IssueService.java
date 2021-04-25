package ru.fondorg.mytracksrv.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.domain.QIssue;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.exception.NotFoundException;
import ru.fondorg.mytracksrv.repo.IssueRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    private final ProjectService projectService;

    private final QueryService queryService;

    public static final String ISSUE_SCOPE_OPENED = "open";
    public static final String ISSUE_SCOPE_CLOSED = "closed";
    public static final String ISSUE_SCOPE_ALL = "all";

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #user.id)")
    public Issue saveIssue(Issue issue, Long projectId, User user) {
        issue.setAuthor(user);
        projectService.getProject(projectId, user.getId())
                .ifPresentOrElse(issue::setProject,
                        () -> {
                            throw new NotFoundException("Project not found");
                        });
        if (issue.getId() == null) {
            long count = issueRepository.countByProjectId(projectId);
            issue.setPid(++count);
            issue.setCreated(LocalDateTime.now());
        }
        if (null == issue.getClosed()) {
            issue.setClosed(false);
        }
        return issueRepository.save(issue);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #user.id)")
    public Optional<Issue> getProjectIssue(Long projectId, Long issueId, User user) {
        return issueRepository.findById(issueId);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public Page<Issue> getProjectIssues(Long projectId, String userId, MultiValueMap<String, String> params) {
        return issueRepository.findAll(assembleIssuePredicate(projectId, params), queryService.getPageable(params));
    }


    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public void deleteIssue(Long projectId, String userId, Long issueId) {
        issueRepository.deleteById(issueId);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public void deleteAllProjectIssues(Long projectId, String userId) {
        issueRepository.deleteByProjectId(projectId);
    }

//    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
//    public Page<Issue> findProjectIssuesByTag(Long projectId, String userId, Long tagId, int page, int size, String scope) {
//        if (scope.equals(ISSUE_SCOPE_OPENED)) {
//            return issueRepository.findByProjectIdAndClosedAndTags_Id(projectId, false, tagId, PageRequest.of(page, size));
//        } else if (scope.equals(ISSUE_SCOPE_CLOSED)) {
//            return issueRepository.findByProjectIdAndClosedAndTags_Id(projectId, true, tagId, PageRequest.of(page, size));
//        }
//        return issueRepository.findByProjectIdAndTags_Id(projectId, tagId, PageRequest.of(page, size));
//    }

    public Predicate assembleIssuePredicate(Long projectId, MultiValueMap<String, String> params) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(ofProject(projectId)).
                and(whereScope(Objects.requireNonNullElse(params.getFirst("scope"), ISSUE_SCOPE_OPENED)))
        .and(withTags(Objects.requireNonNullElse(params.get("tags[]"), Collections.emptyList())));
        return builder;
    }

    private BooleanExpression ofProject(Long projectId) {
        return QIssue.issue.project.id.eq(projectId);
    }

    private Predicate whereScope(String scope) {
        BooleanBuilder bb = new BooleanBuilder();
        if (scope.equals(ISSUE_SCOPE_OPENED)) {
            bb.and(QIssue.issue.closed.eq(false));
        } else if (scope.equals(ISSUE_SCOPE_CLOSED)) {
            bb.and(QIssue.issue.closed.eq(true));
        }
        return bb;
    }

    private Predicate withTags(List<String> tags) {
        BooleanBuilder bb = new BooleanBuilder();
        tags.forEach(t -> bb.or(QIssue.issue.tags.any().name.eq(t)));
        return bb;
    }

}
