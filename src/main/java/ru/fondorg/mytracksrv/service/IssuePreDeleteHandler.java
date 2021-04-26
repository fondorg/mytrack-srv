package ru.fondorg.mytracksrv.service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class IssuePreDeleteHandler extends AbstractPreDeletionHandler<IssuePreDeletionAction, Long> {
    public IssuePreDeleteHandler(List<IssuePreDeletionAction> actions) {
        super(actions);
    }
}
