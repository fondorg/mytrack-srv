package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import ru.fondorg.mytracksrv.exception.ModelDeleteException;
import ru.fondorg.mytracksrv.repo.IssueRepository;

/**
 * Deletes all project issues before project deletion
 */
@RequiredArgsConstructor
public class ProjectIssuesPreDeletion extends ProjectPreDeletionAction {
    private final IssueRepository issueRepository;

    @Override
    public boolean preDelete(Long targetId, @Nullable String userId) throws ModelDeleteException {
        issueRepository.deleteByProjectId(targetId);
        return true;
    }
}
