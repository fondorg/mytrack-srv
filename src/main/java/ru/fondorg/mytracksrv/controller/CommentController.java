package ru.fondorg.mytracksrv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ru.fondorg.mytracksrv.domain.Comment;
import ru.fondorg.mytracksrv.exception.NotFoundException;
import ru.fondorg.mytracksrv.service.CommentService;
import ru.fondorg.mytracksrv.service.ServletRequestAttributesService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final ServletRequestAttributesService requestAttributesService;

    @PostMapping(ApiV1Paths.ISSUE_COMMENTS)
    public Comment saveComment(@PathVariable Long projectId, @PathVariable Long issueId, @RequestBody Comment comment,
                               HttpServletRequest request) {
        return commentService.saveComment(comment, projectId, issueId, requestAttributesService.getUserFromRequest(request));
    }

    @GetMapping(ApiV1Paths.ISSUE_COMMENT)
    public Comment getComment(@PathVariable Long projectId, @PathVariable Long commentId, HttpServletRequest request) {
        return commentService.getComment(commentId, projectId, requestAttributesService.getUserFromRequest(request))
                .orElseThrow(() -> new NotFoundException("Comment not found"));
    }

    @GetMapping(ApiV1Paths.ISSUE_COMMENTS)
    public Page<Comment> getIssueComments(@PathVariable Long projectId, @PathVariable Long issueId,
                                          @RequestParam MultiValueMap<String, String> qParams,
                                          HttpServletRequest request) {
        return commentService.getIssueComments(issueId, projectId,
                requestAttributesService.getUserFromRequest(request), qParams);
    }

    @DeleteMapping(ApiV1Paths.ISSUE_COMMENT)
    public void deleteComment(@PathVariable Long projectId, @PathVariable Long commentId, HttpServletRequest request) {
        commentService.deleteComment(commentId, projectId,
                requestAttributesService.getUserFromRequest(request));
    }
}
