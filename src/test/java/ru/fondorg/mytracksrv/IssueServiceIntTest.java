package ru.fondorg.mytracksrv;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.domain.Project;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.repo.UserRepository;
import ru.fondorg.mytracksrv.service.IssueService;
import ru.fondorg.mytracksrv.service.ParamMapBuilder;
import ru.fondorg.mytracksrv.service.ProjectService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

        Page<Issue> page = issueService.getProjectIssues(project.getId(), user.getId(),
                ParamMapBuilder.newMap().add("page", "1").add("size", "20").build());
        assertThat(page.getContent().size()).isEqualTo(1);
    }

    /**
     * Tries to create issue in the unauthorized project
     * Should fail with exception
     */
    @Test
    @WithMockUser
    public void saveIssueUnauthorized() {
        assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(() -> {
            User user1 = MytrackTestUtils.instanceOfUser("111");
            User user2 = MytrackTestUtils.instanceOfUser("222");
            Project project = MytrackTestUtils.instanceOfProject();
            projectService.createProject(project, user1);
            createTestIssue(user2, project);
        });
    }

    /**
     * Test of getting issue by issue id for the specific project
     */
    @Test
    @WithMockUser
    public void getIssue() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = MytrackTestUtils.instanceOfProject();
        project = projectService.createProject(project, user);
        Issue issue = createTestIssue(user, project);
        issueService.getProjectIssue(project.getId(), issue.getId(), user).orElseThrow();
    }

    @Test
    @WithMockUser
    public void updateIssue() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = MytrackTestUtils.instanceOfProject();
        project = projectService.createProject(project, user);
        Issue issue = createTestIssue(user, project);
        String updatedTitle = "New issue title";
        issue.setTitle(updatedTitle);
        Issue updatedIssue = issueService.saveIssue(issue, project.getId(), user);
        assertThat(updatedIssue.getTitle()).isEqualTo(updatedTitle);
    }

    @Test
    @WithMockUser
    public void findAllUserIssues() {
        //TODO
    }

    /**
     * Find list of project issues with pagination
     */
    @Test
    @WithMockUser
    public void findAllProjectIssue() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = MytrackTestUtils.instanceOfProject();
        project = projectService.createProject(project, user);
        String fTitle = "Issue %d";
        String fDescription = "Description of issue %d";
        int totalIssues = 20;
        for (int i = 0; i < totalIssues; i++) {
            Issue issue = new Issue();
            issue.setAuthor(user);
            issue.setProject(project);
            issue.setTitle(String.format(fTitle, i));
            issue.setDescription(String.format(fDescription, i));
            issueService.saveIssue(issue, project.getId(), user);
        }
        int pageSize = 5;
        int pageNum = 1;
        Page<Issue> page = issueService.getProjectIssues(project.getId(), user.getId(),
                ParamMapBuilder.newMap().add("page", String.valueOf(pageNum)).add("size", String.valueOf(pageSize)).build());
        assertThat(page.getContent().size()).isEqualTo(pageSize);
        assertThat(page.getTotalPages()).isEqualTo(totalIssues / pageSize);
    }

    /**
     * Helper test method that creates new issue instance in the database
     *
     * @param user    the creator of the issue
     * @param project the project the issue belongs to
     * @return created issue
     */
    private Issue createTestIssue(User user, Project project) {
        Issue issue = new Issue();
        issue.setAuthor(user);
        issue.setProject(project);
        issue.setTitle("test issue");
        issue.setDescription("test issue description");
        return issueService.saveIssue(issue, project.getId(), user);
    }
}
