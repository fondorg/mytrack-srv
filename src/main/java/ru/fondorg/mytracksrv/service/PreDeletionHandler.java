package ru.fondorg.mytracksrv.service;

import ru.fondorg.mytracksrv.exception.ModelDeleteException;

/**
 * Manages the pre deletion actions for the specified target
 * @param <T> The type of the final target to be deleted
 */
public interface PreDeletionHandler<T> {

    /**
     * Handle all pre deletion actions for the specified target
     * @param target The target to be deleted
     * @param userId The user's id that is triggering the deletion
     * @return true if all pre deletion actions are executed successfully, false otherwise
     */
    boolean handlePreDeletionActions(T target, String userId) throws ModelDeleteException;
}
