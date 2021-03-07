package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import ru.fondorg.mytracksrv.exception.ModelDeleteException;
import ru.fondorg.mytracksrv.repo.ProjectRepository;

@RequiredArgsConstructor
public class ProjectParticipantsPreDeletion extends ProjectPreDeletionAction {
    private final ProjectRepository projectRepository;

    @Override
    public boolean preDelete(Long targetId, String userId) throws ModelDeleteException {
        projectRepository.findById(targetId).ifPresent(project -> {
            project.getParticipants().clear();
            projectRepository.save(project);
        });
//        projectParticipantRepository.deleteByProjectId(targetId);
        return true;
    }
}
