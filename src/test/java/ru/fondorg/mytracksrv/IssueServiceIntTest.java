package ru.fondorg.mytracksrv;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.domain.Project;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.repo.UserRepository;
import ru.fondorg.mytracksrv.service.IssueService;
import ru.fondorg.mytracksrv.service.ProjectService;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IssueServiceIntTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IssueService issueService;

    @Test
    @WithMockUser
    public void saveIssue() throws Exception {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = MytrackTestUtils.instanceOfProject();
        project = projectService.createProject(project, user);
        Issue issue = createTestIssue(user, project);
        List<Issue> issues = projectService.getProjectIssues(user.getId(), project.getId());
        Assertions.assertThat(issues.size()).isEqualTo(1);
    }

    /**
     * Tries to create issue in the unauthorized project
     * Should fail with exception
     */
    @Test
    @WithMockUser
    public void saveIssueUnauthorized() {
        Assertions.assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(() -> {
            User user1 = MytrackTestUtils.instanceOfUser("111");
            User user2 = MytrackTestUtils.instanceOfUser("222");
            Project project = MytrackTestUtils.instanceOfProject();
            projectService.createProject(project, user1);
            createTestIssue(user2, project);
        });
    }

    @Test
    @WithMockUser
    public void getIssue() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = MytrackTestUtils.instanceOfProject();
        project = projectService.createProject(project, user);
        createTestIssue(user, project);
        Issue issue = issueService.
    }

    @Test
    @WithMockUser
    public void updateIssue() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = MytrackTestUtils.instanceOfProject();
        project = projectService.createProject(project, user);
        createTestIssue(user, project);
//        Issue issue = issueService.
//        TODO
    }

    @Test
    @WithMockUser
    public void findAllUserIssues() {
        //TODO
    }

    @Test
    public void findAllProjectIssue() {
        //TODO
    }

    private Issue createTestIssue(User user, Project project) {
        Issue issue = new Issue();
        issue.setAuthor(user);
        issue.setProject(project);
        issue.setTitle("test issue");
        issue.setDescription("test issue description");
        return issueService.saveIssue(issue, user);
    }
}
