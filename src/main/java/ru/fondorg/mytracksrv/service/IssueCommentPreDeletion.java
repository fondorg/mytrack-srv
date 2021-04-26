package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import ru.fondorg.mytracksrv.exception.ModelDeleteException;
import ru.fondorg.mytracksrv.repo.CommentRepository;

@RequiredArgsConstructor
public class IssueCommentPreDeletion extends IssuePreDeletionAction {

    private final CommentRepository commentRepository;

    @Override
    public boolean preDelete(Long targetId, String userId) throws ModelDeleteException {
        commentRepository.deleteByIssueId(targetId);
        return true;
    }
}
