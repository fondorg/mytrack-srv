package ru.fondorg.mytracksrv.service;

import lombok.extern.slf4j.Slf4j;
import ru.fondorg.mytracksrv.exception.ModelDeleteException;

import java.util.List;

/**
 * Runs all found custom pre deletion actions for a specified model class
 */
@Slf4j
//@RequiredArgsConstructor
public class ProjectPreDeleteHandler implements PreDeletionHandler<Long> {

    private final List<ProjectPreDeletionAction> actions;

    public ProjectPreDeleteHandler(List<ProjectPreDeletionAction> actions) {
        this.actions = actions;
    }

    @Override
    public boolean handlePreDeletionActions(Long target, String userId) throws ModelDeleteException {
        for (PreDeletionAction<Long> action : actions) {
            if (!action.preDelete(target, userId)) {
                log.warn("Pre deletion action failed: {}", action.getClass().getSimpleName());
                return false;
            }
        }
        return true;
    }
}
