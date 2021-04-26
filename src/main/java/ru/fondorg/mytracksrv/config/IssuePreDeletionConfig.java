package ru.fondorg.mytracksrv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import ru.fondorg.mytracksrv.repo.CommentRepository;
import ru.fondorg.mytracksrv.service.IssueCommentPreDeletion;
import ru.fondorg.mytracksrv.service.IssuePreDeleteHandler;
import ru.fondorg.mytracksrv.service.IssuePreDeletionAction;

import java.util.List;

@Configuration
public class IssuePreDeletionConfig {
    @Bean
    public IssuePreDeleteHandler issuePreDeleteHandler(List<IssuePreDeletionAction> actions) {
        return new IssuePreDeleteHandler(actions);
    }

    @Bean
    @Order(100)
    public IssuePreDeletionAction issueCommentsPreDeletion(CommentRepository commentRepository) {
        return new IssueCommentPreDeletion(commentRepository);
    }
}
