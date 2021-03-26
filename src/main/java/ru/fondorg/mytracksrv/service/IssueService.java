package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.exception.NotFoundException;
import ru.fondorg.mytracksrv.repo.IssueRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    private final ProjectService projectService;


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
        if (null == issue.getClosed()) {
            issue.setClosed(false);
        }
        return issueRepository.save(issue);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public Issue updateIssue(Long projectId, Long issueId, Issue issue, String userId) {
        Issue issue1
        return issueRepository.save(issue);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #user.id)")
    public Optional<Issue> getProjectIssue(Long projectId, Long issueId, User user) {
        return issueRepository.findById(issueId);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public Page<Issue> getProjectIssues(Long projectId, String userId, int page, int size) {
//        return issueRepository.findByProjectId(projectId, PageRequest.of(page, size));
        return getProjectIssues(projectId, userId, page, size, ISSUE_SCOPE_OPENED);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public Page<Issue> getProjectIssues(Long projectId, String userId, int page, int size, String scope) {
        if (scope.equals(ISSUE_SCOPE_OPENED)) {
            return issueRepository.findByProjectIdAndClosed(projectId, false, PageRequest.of(page, size));
        } else if (scope.equals(ISSUE_SCOPE_CLOSED)) {
            return issueRepository.findByProjectIdAndClosed(projectId, true, PageRequest.of(page, size));
        }
        return issueRepository.findByProjectId(projectId, PageRequest.of(page, size));
    }


    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public void deleteIssue(Long projectId, String userId, Long issueId) {
        issueRepository.deleteById(issueId);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public void deleteAllProjectIssues(Long projectId, String userId) {
        issueRepository.deleteByProjectId(projectId);
    }
}
