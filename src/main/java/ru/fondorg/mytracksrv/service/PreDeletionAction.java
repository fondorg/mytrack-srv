package ru.fondorg.mytracksrv.service;

import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import ru.fondorg.mytracksrv.exception.ModelDeleteException;


/**
 * Interface to use for complex deletion actions that should be prepended with some other actions or checks
 * @param <ID> The type of the id of the model that should be deleted
 */
public interface PreDeletionAction<ID> {

    /**
     * Called before target deletion, to prepend deletion with custom functionality.
     * It can influence the deletion of the final target
     * @param targetId The final target(model) id that should be deleted
     * @param userId Session user id
     * @return true if it is ok to proceed with target deletion, false otherwise
     * @throws ModelDeleteException if deletion goes wrong
     */
    @Transactional
    boolean preDelete(ID targetId, @Nullable String userId) throws ModelDeleteException;

}
