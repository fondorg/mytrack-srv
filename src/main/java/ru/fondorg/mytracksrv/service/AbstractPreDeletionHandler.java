package ru.fondorg.mytracksrv.service;

import lombok.extern.slf4j.Slf4j;
import ru.fondorg.mytracksrv.exception.ModelDeleteException;

import java.util.List;

@Slf4j
public abstract class AbstractPreDeletionHandler<P extends PreDeletionAction<ID>, ID> implements PreDeletionHandler<ID> {

    private final List<P> actions;

    public AbstractPreDeletionHandler(List<P> actions) {
        this.actions = actions;
    }

    @Override
    public boolean handlePreDeletionActions(ID target, String userId) throws ModelDeleteException {
        for (P action : actions) {
            if (!action.preDelete(target, userId)) {
                log.warn("Pre deletion action failed: {}", action.getClass().getSimpleName());
                return false;
            }
        }
        return true;
    }
}
