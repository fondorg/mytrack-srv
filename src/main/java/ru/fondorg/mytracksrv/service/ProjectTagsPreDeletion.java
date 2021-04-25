package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import ru.fondorg.mytracksrv.exception.ModelDeleteException;
import ru.fondorg.mytracksrv.repo.TagRepository;

@RequiredArgsConstructor
public class ProjectTagsPreDeletion extends ProjectPreDeletionAction {
    private final TagRepository tagRepository;

    @Override
    public boolean preDelete(Long targetId, String userId) throws ModelDeleteException {
        tagRepository.deleteByProjectId(targetId);
        return true;
    }
}
