package ru.fondorg.mytracksrv;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.fondorg.mytracksrv.domain.Comment;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.domain.Project;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.repo.CommentRepository;
import ru.fondorg.mytracksrv.service.CommentService;
import ru.fondorg.mytracksrv.service.IssueService;
import ru.fondorg.mytracksrv.service.QueryService;
import ru.fondorg.mytracksrv.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentServiceTest {
    @Autowired
    TestBootstrap testBootstrap;
    @Autowired
    IssueService issueService;
    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    QueryService queryService;

    private static final String COMMENT_TEXT = "This is a new comment";

    @Test
    @WithMockUser
    public void saveComment() throws Exception {
        TestBootstrap.TestStructure ts = testBootstrap.getTestStructure().withIssue1();
        Comment comment = new Comment();
        comment.setText(COMMENT_TEXT);
        Comment reloaded = commentService.saveComment(comment, ts.project.getId(), ts.issue1.getId(), ts.user);
        assertThat(commentRepository.findByIssueId(ts.issue1.getId(), testBootstrap.getPageable(1, 20))
                .getTotalElements()).isEqualTo(1);
        assertThat(reloaded.getText()).isEqualTo(COMMENT_TEXT);
        assertThat(reloaded.getIssue().getId()).isEqualTo(ts.issue1.getId());
    }

    @Test
    @WithMockUser
    public void unauthorizedComment() throws Exception {
        TestBootstrap.TestStructure ts = testBootstrap.getTestStructure().withIssue1().withAlienUser();
        Comment comment = new Comment();
        comment.setText(COMMENT_TEXT);
        assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(() -> {
            commentService.saveComment(comment, ts.project.getId(), ts.issue1.getId(), ts.alienUser);
        });
    }

    @Test
    @WithMockUser
    public void getComment() throws Exception {
        TestBootstrap.TestStructure ts = testBootstrap.getTestStructure().withIssue1();
        Comment comment = new Comment();
        comment.setText(COMMENT_TEXT);
        Comment reloaded = commentService.saveComment(comment, ts.project.getId(), ts.issue1.getId(), ts.user);

        Optional<Comment> saved = commentService.getComment(reloaded.getId(), ts.project.getId(), ts.user);
        assertThat(saved).isPresent();
        saved.ifPresent(c -> {
            assertThat(c.getText()).isEqualTo(COMMENT_TEXT);
            assertThat(c.getAuthor().getId()).isEqualTo(ts.user.getId());
            assertThat(c.getIssue().getId()).isEqualTo(ts.issue1.getId());
        });
    }

    private Issue createIssue(Project project, User user) {
        Issue issue = new Issue();
        issue.setTitle("Issue title");
        issue.setDescription("Issue description");
        return issueService.saveIssue(issue, project.getId(), user);
    }

}
