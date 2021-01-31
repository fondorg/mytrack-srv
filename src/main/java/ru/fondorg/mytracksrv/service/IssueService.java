package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.repo.IssueRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    private final ProjectService projectService;

    @PreAuthorize("@projectService.isUserParticipatesInProject(#issue.project.id, #user.id)")
    public Issue saveIssue(Issue issue, User user) {
        issue.setAuthor(user);
        return issueRepository.save(issue);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#issue.project.id, #user.id)")
    public Issue updateIssue(Issue issue, User user) {
        return issueRepository.save(issue);
    }

    public List<Issue> findAllIssues() {
        return null;
    }
}
