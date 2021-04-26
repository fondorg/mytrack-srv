package ru.fondorg.mytracksrv.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.*;
import ru.fondorg.mytracksrv.domain.Comment;
import ru.fondorg.mytracksrv.domain.dto.CommentDto;
import ru.fondorg.mytracksrv.exception.NotFoundException;
import ru.fondorg.mytracksrv.service.CommentService;
import ru.fondorg.mytracksrv.service.ServletRequestAttributesService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final ServletRequestAttributesService requestAttributesService;
    private final ModelMapper modelMapper;

    @PostMapping(ApiV1Paths.ISSUE_COMMENTS)
    public CommentDto saveComment(@PathVariable Long projectId, @PathVariable Long issueId, @RequestBody Comment comment,
                               HttpServletRequest request) {
        return modelMapper.map(commentService.saveComment(comment, projectId, issueId,
                requestAttributesService.getUserFromRequest(request)), CommentDto.class);
    }

    @GetMapping(ApiV1Paths.ISSUE_COMMENT)
    public Comment getComment(@PathVariable Long projectId, @PathVariable Long commentId, HttpServletRequest request) {
        return commentService.getComment(commentId, projectId, requestAttributesService.getUserFromRequest(request))
                .orElseThrow(() -> new NotFoundException("Comment not found"));
    }

    @GetMapping(ApiV1Paths.ISSUE_COMMENTS)
    public List<CommentDto> getIssueComments(@PathVariable Long projectId, @PathVariable Long issueId,
                                             /*@RequestParam MultiValueMap<String, String> qParams,*/
                                             HttpServletRequest request) {
        List<Comment> comments = commentService.getIssueComments(issueId, projectId,
                requestAttributesService.getUserFromRequest(request));
        return modelMapper.map(comments, new TypeToken<List<CommentDto>>(){}.getType());
//        return page.map(comment -> modelMapper.map(comment, CommentDto.class));
    }

    @DeleteMapping(ApiV1Paths.ISSUE_COMMENT)
    public void deleteComment(@PathVariable Long projectId, @PathVariable Long commentId, HttpServletRequest request) {
        commentService.deleteComment(commentId, projectId,
                requestAttributesService.getUserFromRequest(request));
    }
}
