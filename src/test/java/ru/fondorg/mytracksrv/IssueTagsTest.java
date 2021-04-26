package ru.fondorg.mytracksrv;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.domain.Project;
import ru.fondorg.mytracksrv.domain.Tag;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.repo.IssueRepository;
import ru.fondorg.mytracksrv.repo.UserRepository;
import ru.fondorg.mytracksrv.service.IssueService;
import ru.fondorg.mytracksrv.service.ParamMapBuilder;
import ru.fondorg.mytracksrv.service.TagService;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IssueTagsTest {
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    TestBootstrap testBootstrap;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagService tagService;
    @Autowired
    IssueService issueService;

    @Test
    @WithMockUser
    public void tagCreation() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project1 = testBootstrap.bootstrapProject("Project 1", user);
        Project project2 = testBootstrap.bootstrapProject("Project 2", user);
        Tag commonTag = new Tag("CommonTag", "#ffffff");
        tagService.saveTag(commonTag, user.getId());
        Tag project1Tag = new Tag("Project1Tag", "#ffffff");
        tagService.saveProjectTag(project1Tag, project1.getId(), user.getId());
        Tag project2Tag = new Tag("Project2Tag", "#ffffff");
        tagService.saveProjectTag(project2Tag, project2.getId(), user.getId());

        List<Tag> project1Tags = tagService.getProjectTags(project1.getId(), user.getId());
        List<Tag> project2Tags = tagService.getProjectTags(project2.getId(), user.getId());
        assertThat(project1Tags).isNotNull();
        assertThat(project1Tags.size()).isEqualTo(2);

        assertThat(project2Tags).isNotNull();
        assertThat(project2Tags.size()).isEqualTo(2);
    }

    @Test
    @WithMockUser
    public void tagsAccessViolation() {
        User user = MytrackTestUtils.instanceOfUser("111");
        User alien = userRepository.save(MytrackTestUtils.instanceOfUser("alien"));
        Project project = testBootstrap.bootstrapProject("Project 1", user);
        Tag projectTag = new Tag("ProjectTag", "#ffffff");
        tagService.saveProjectTag(projectTag, project.getId(), user.getId());
        assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(() ->
                tagService.getProjectTags(project.getId(), alien.getId()));
    }

    @Test
    @WithMockUser
    public void getCommonTags() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = testBootstrap.bootstrapProject("Project 1", user);
        Tag projectTag = new Tag("ProjectTag", "#ffffff");
        Tag commonTag1 = new Tag("Tag1", "#ffffff");
        Tag commonTag2 = new Tag("Tag2", "#ffffff");
        tagService.saveProjectTag(projectTag, project.getId(), user.getId());
        tagService.saveTag(commonTag1, user.getId());
        tagService.saveTag(commonTag2, user.getId());
        Page<Tag> tags = tagService.getCommonTags(ParamMapBuilder.newMap().add("page", "1").add("size", "20").build());
        assertThat(tags.getContent().size()).isEqualTo(2);
    }

    @Test
    public void commonTagsAccessViolation() {
        assertThatExceptionOfType(AuthenticationCredentialsNotFoundException.class).isThrownBy(() -> {
            Tag commonTag2 = new Tag("CommonTag", "#ffffff");
            tagService.saveTag(commonTag2, "111");
        });
    }

    @Test
    @WithMockUser
    public void deleteProjectTag() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = testBootstrap.bootstrapProject("Project 1", user);
        Tag projectTag = new Tag("Tag1", "#ffffff", project);
        projectTag = tagService.saveTag(projectTag, user.getId());
        tagService.deleteProjectTag(projectTag.getId(), project.getId(), user.getId());

        List<Tag> projectTags = tagService.getProjectTags(project.getId(), user.getId());
        assertThat(projectTags).isEmpty();
    }

    @Test
    @WithMockUser
    public void deleteDagAccessViolation() {
        //assert
        User user = MytrackTestUtils.instanceOfUser("111");
        User alien = userRepository.save(MytrackTestUtils.instanceOfUser("alien"));
        Project project = testBootstrap.bootstrapProject("Project 1", user);
        Tag projectTag = tagService.saveTag(new Tag("ProjectTag", "#ffffff", project), user.getId());
        //act and assert
        assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(() -> {
            tagService.deleteProjectTag(projectTag.getId(), project.getId(), alien.getId());
        });
    }

    @Test
    @WithMockUser
    public void getProjectTagById() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = testBootstrap.bootstrapProject("Project 1", user);
        Tag projectTag = tagService.saveTag(new Tag("ProjectTag", "#ffffff", project), user.getId());
        assertThat(tagService.getProjectTag(project.getId(), projectTag.getId(), user.getId())).isPresent();
    }

    @Test
    @WithMockUser
    public void getCommonTagById() {
        tagService.saveTag(new Tag("CommonTag", "#ffffff"), "111");
        assertThat(tagService.getCommonTag(1L)).isPresent();
    }

    @Test
    @WithMockUser
    public void createAndFindIssueTag() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = testBootstrap.bootstrapProject("Project 1", user);
        Issue issue = new Issue();
        issue.setTitle("Test issue");
        issue.setDescription("Test issue");
        issue.setTags(new HashSet<>());
        issue.getTags().add(new Tag("IssueTag", "#000000", project));
        issue = issueService.saveIssue(issue, project.getId(), user);
        List<Tag> issueTags = issueRepository.findIssueTags(issue.getId());
        assertThat(issueTags.size()).isEqualTo(issue.getTags().size());
        List<Tag> projectTags = tagService.getProjectTags(project.getId(), user.getId());
        assertThat(projectTags.size()).isEqualTo(issueTags.size());
    }
}
