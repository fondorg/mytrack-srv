package ru.fondorg.mytracksrv;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.domain.Project;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.service.*;

/**
 * Helper class to bootstrap project for tests
 */
@RequiredArgsConstructor
@Service
public class TestBootstrap {

    public static final String USER_ID = "user";
    public static final String ALIEN_USER_ID = "alien";

    private final ProjectService projectService;
    private final QueryService queryService;
    private final UserService userService;
    private final IssueService issueService;



    public Project bootstrapProject(String title) {
        return bootstrapProject(title, null);
    }

    public Project bootstrapProject(String title, User user) {
        Project project = MytrackTestUtils.instanceOfProject(title);
        if (user == null) {
            user = MytrackTestUtils.instanceOfUser("111");
        }
        return projectService.createProject(project, user);
    }

    public Pageable getPageable(int page, int size) {
        return queryService.getPageable(
                ParamMapBuilder.newMap()
                        .add("page", String.valueOf(page))
                        .add("size", String.valueOf(page)).build());
    }

    public TestStructure getTestStructure() {
        return new TestStructure();
    }

    public class TestStructure {
        public Project project;

        public User user;
        public User alienUser;

        public Issue issue1;
        public Issue issue2;

        public TestStructure() {
            user = new User();
            user.setId(USER_ID);
            user.setUsername(USER_ID);
            user.setFirstName("Project");
            user.setLastName("User");
            project = bootstrapProject("Project 1", user);
        }

        public TestStructure withAlienUser() {
            User user = new User();
            user.setId(ALIEN_USER_ID);
            user.setUsername(ALIEN_USER_ID);
            user.setFirstName("Alien");
            user.setLastName("User");
            alienUser = userService.findByIdOrCreate(user);
            return this;
        }

        public TestStructure withIssue1() {
            Issue issue = new Issue();
            issue.setTitle("Issue 1");
            issue.setDescription("Issue description 1");
            issue1 = issueService.saveIssue(issue, project.getId(), user);
            return this;
        }

        public TestStructure withIssue2() {
            Issue issue = new Issue();
            issue.setTitle("Issue 2");
            issue.setDescription("Issue description 2");
            issue2 = issueService.saveIssue(issue, project.getId(), user);
            return this;
        }
    }
}
