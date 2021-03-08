package ru.fondorg.mytracksrv;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.fondorg.mytracksrv.domain.Project;
import ru.fondorg.mytracksrv.domain.Tag;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.repo.IssueRepository;
import ru.fondorg.mytracksrv.repo.UserRepository;
import ru.fondorg.mytracksrv.service.TagService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IssueTagsTest {
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    ProjectBootstrap projectBootstrap;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagService tagService;

    @Test
    @WithMockUser
    public void tagCreation() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project1 = projectBootstrap.bootstrapProject("Project 1", user);
        Project project2 = projectBootstrap.bootstrapProject("Project 2", user);
        Tag commonTag = new Tag("CommonTag", "#ffffff");
        tagService.newTag(commonTag, user.getId());
        Tag project1Tag = new Tag("Project1Tag", "#ffffff", project1);
        tagService.newTag(project1Tag, user.getId());
        Tag project2Tag = new Tag("Project2Tag", "#ffffff", project2);
        tagService.newTag(project2Tag, user.getId());

        List<Tag> project1Tags = tagService.getProjectTags(project1.getId(), user.getId());
        List<Tag> project2Tags = tagService.getProjectTags(project2.getId(), user.getId());
        assertThat(project1Tags).isNotNull();
        assertThat(project1Tags.size()).isEqualTo(2);

        assertThat(project2Tags).isNotNull();
        assertThat(project2Tags.size()).isEqualTo(2);
    }


    @Test
    @WithMockUser
    public void tagsAccessViolationCheck() {
        User user = MytrackTestUtils.instanceOfUser("111");
        User alien = userRepository.save(MytrackTestUtils.instanceOfUser("alien"));
        Project project = projectBootstrap.bootstrapProject("Project 1", user);
        Tag projectTag = new Tag("ProjectTag", "#ffffff", project);
        tagService.newTag(projectTag, user.getId());
        assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(() ->
                tagService.getProjectTags(project.getId(), alien.getId()));
    }

    @Test
    @WithMockUser
    public void getCommonTags() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = projectBootstrap.bootstrapProject("Project 1", user);
        Tag projectTag = new Tag("ProjectTag", "#ffffff", project);
        Tag commonTag1 = new Tag("Tag1", "#ffffff");
        Tag commonTag2 = new Tag("Tag2", "#ffffff");
        tagService.newTag(projectTag, user.getId());
        tagService.newTag(commonTag1, user.getId());
        tagService.newTag(commonTag2, user.getId());
        Page<Tag> tags = tagService.getCommonTags(0, 20);
        assertThat(tags.getContent().size()).isEqualTo(2);
    }


    @Test
    public void commonTagsAccessViolation() {
        assertThatExceptionOfType(AuthenticationCredentialsNotFoundException.class).isThrownBy(() -> {
            Tag commonTag2 = new Tag("CommonTag", "#ffffff");
            tagService.newTag(commonTag2, "111");
        });
    }


    @Test
    @WithMockUser
    public void deleteProjectTag() {
        User user = MytrackTestUtils.instanceOfUser("111");
        Project project = projectBootstrap.bootstrapProject("Project 1", user);
        Tag projectTag = new Tag("Tag1", "#ffffff", project);
        projectTag = tagService.newTag(projectTag, user.getId());
        tagService.deleteTag(projectTag.getId(), project.getId(), user.getId());

        List<Tag> projectTags = tagService.getProjectTags(project.getId(), user.getId());
        assertThat(projectTags).isEmpty();
    }
}