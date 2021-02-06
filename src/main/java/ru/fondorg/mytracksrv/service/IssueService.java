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

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #user.id)")
    public Issue saveIssue(Issue issue, Long projectId, User user) {
        issue.setAuthor(user);
        projectService.getProject(projectId, user.getId())
                .ifPresentOrElse(issue::setProject,
                        () -> {
                            throw new NotFoundException("Project not found");
                        });
        return issueRepository.save(issue);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#issue.project.id, #user.id)")
    public Issue updateIssue(Issue issue, User user) {
        return issueRepository.save(issue);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #user.id)")
    public Optional<Issue> getProjectIssue(Long projectId, Long issueId, User user) {
        return issueRepository.findById(issueId);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public Page<Issue> getProjectIssues(Long projectId, String userId, int page, int size) {
        return issueRepository.findByProjectId(projectId, PageRequest.of(page, size));
    }
}
