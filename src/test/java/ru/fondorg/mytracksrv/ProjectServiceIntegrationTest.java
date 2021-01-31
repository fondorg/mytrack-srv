package ru.fondorg.mytracksrv;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.fondorg.mytracksrv.domain.ParticipantRole;
import ru.fondorg.mytracksrv.domain.Project;
import ru.fondorg.mytracksrv.domain.ProjectParticipant;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.repo.ProjectParticipantRepository;
import ru.fondorg.mytracksrv.repo.ProjectRepository;
import ru.fondorg.mytracksrv.repo.UserRepository;
import ru.fondorg.mytracksrv.service.ProjectService;
import ru.fondorg.mytracksrv.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.fondorg.mytracksrv.MytrackTestUtils.instanceOfProject;
import static ru.fondorg.mytracksrv.MytrackTestUtils.instanceOfUser;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@EnableConfigurationProperties
//@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
public class ProjectServiceIntegrationTest {


    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    ProjectParticipantRepository projectParticipantRepository;

    @Autowired
    ProjectService projectService;

    @Test
    public void testCreate() throws Exception {
        Project project = instanceOfProject();
        User user = instanceOfUser("111");
        project = projectService.createProject(project, user);
        project = projectRepository.findById(project.getId()).get();
        user = userRepository.findById(user.getId()).get();
        assertParticipantsCount(project.getId(), project.getParticipants().size());
    }

    @Test
    public void addUserToProject() throws Exception {
        Project project = instanceOfProject();
        User owner = instanceOfUser("111");
        project = projectService.createProject(project, owner);

        User secondUser = instanceOfUser("222");
        projectService.addUserToProject(project, secondUser, ParticipantRole.PARTICIPANT);
        assertParticipantsCount(project.getId(), 2);
    }

    @Test
    public void userIsParticipatingInProject() throws Exception {
        User owner = instanceOfUser("111");
        Project project = createTestProject(owner);
        boolean isParticipant = projectService.isUserParticipatesInProject(project.getId(), owner.getId());
        assertThat(isParticipant).isTrue();
    }

    @Test
    public void userIsNotParticipatingInProject() throws Exception {
        User owner = instanceOfUser("111");
        Project project = createTestProject(owner);
        User anotherUser = instanceOfUser("222");
        anotherUser = userService.findByIdOrCreate(anotherUser);
        boolean isParticipant = projectService.isUserParticipatesInProject(project.getId(), anotherUser.getId());
        assertThat(isParticipant).isFalse();
    }

    @Test
    public void removeUserFromProject() throws Exception {
        User owner = instanceOfUser("111");
        userRepository.save(owner);
        Project project = createTestProject(owner);
        User anotherUser = instanceOfUser("222");
        project = projectService.addUserToProject(project, anotherUser, ParticipantRole.PARTICIPANT);
        assertParticipantsCount(project.getId(), 2);

        projectService.removeUserFromProject(project, anotherUser);
        project = projectRepository.findById(project.getId()).get();
        assertParticipantsCount(project.getId(), 1);
    }

    @Test
    public void getAllProjectIssues() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = createTestProject(user);
    }

    @Test
    @WithMockUser
    public void getProject() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = createTestProject(user);
        projectService.getProject(project.getId(), user.getId()).ifPresent(p -> {
            assertThat(p.getId()).isEqualTo(project.getId());
        });
    }


    private Project createTestProject(User user) {
        Project project = instanceOfProject();
        return projectService.createProject(project, user);
    }

    private void assertParticipantsCount(Long projectId, int expected) {
        List<ProjectParticipant> participants = projectParticipantRepository.findDistinctByProjectId(projectId);
        assertThat(participants.size()).isEqualTo(expected);
    }
}

