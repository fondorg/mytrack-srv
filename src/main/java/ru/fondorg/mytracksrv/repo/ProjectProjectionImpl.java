package ru.fondorg.mytracksrv.repo;

import ru.fondorg.mytracksrv.domain.Project;

public class ProjectProjectionImpl implements ProjectProjection {
    private final Project project;

    public ProjectProjectionImpl(Project project) {
        this.project = project;
    }

    @Override
    public Project getProject() {
        return project;
    }
}
