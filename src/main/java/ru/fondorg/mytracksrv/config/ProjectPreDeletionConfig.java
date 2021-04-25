package ru.fondorg.mytracksrv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import ru.fondorg.mytracksrv.repo.IssueRepository;
import ru.fondorg.mytracksrv.repo.TagRepository;
import ru.fondorg.mytracksrv.service.ProjectIssuesPreDeletion;
import ru.fondorg.mytracksrv.service.ProjectPreDeleteHandler;
import ru.fondorg.mytracksrv.service.ProjectPreDeletionAction;
import ru.fondorg.mytracksrv.service.ProjectTagsPreDeletion;

import java.util.List;

/**
 * As far as project deletion may affect many other models it is convenient to create a separate configuration to track
 * pre deletion actions for the project model. It is convenient to view this one config rather then searching pre deletion actions
 * in different places
 */
@Configuration
public class ProjectPreDeletionConfig {

    @Bean
    public ProjectPreDeleteHandler projectPreDeleteHandler(List<ProjectPreDeletionAction> actions) {
        return new ProjectPreDeleteHandler(actions);
    }

    @Bean
    @Order(100)
    public ProjectPreDeletionAction projectIssuesPreDeletion(IssueRepository issueRepository) {
        return new ProjectIssuesPreDeletion(issueRepository);
    }

    @Bean
    @Order(200)
    public ProjectPreDeletionAction projectTagsPreDeletion(TagRepository tagRepository) {
        return new ProjectTagsPreDeletion(tagRepository);
    }
}
