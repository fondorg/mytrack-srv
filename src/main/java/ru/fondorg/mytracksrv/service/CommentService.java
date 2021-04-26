package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.fondorg.mytracksrv.domain.Comment;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.exception.NotFoundException;
import ru.fondorg.mytracksrv.repo.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final ProjectService projectService;

    private final IssueService issueService;

    private final QueryService queryService;

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #user.id)")
    public Comment saveComment(Comment comment, Long projectId, Long issueId, User user) {
        issueService.getProjectIssue(projectId, issueId, user).ifPresentOrElse(comment::setIssue,
                () -> {
                    throw new NotFoundException("Issue not found");
                });
        if (comment.getId() == null) {
            comment.setCreated(LocalDateTime.now());
            comment.setAuthor(user);
        }
        return commentRepository.save(comment);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #user.id)")
    public Optional<Comment> getComment(Long commentId, Long projectId, User user) {
        return commentRepository.findById(commentId);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #user.id)")
    public List<Comment> getIssueComments(Long issueId, Long projectId, User user/*, MultiValueMap<String, String> params*/) {
        return commentRepository.findByIssueIdOrderByCreated(issueId);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #user.id)")
    public void deleteComment(Long commentId, Long projectId, User user) {
        commentRepository.deleteById(commentId);
    }
}
