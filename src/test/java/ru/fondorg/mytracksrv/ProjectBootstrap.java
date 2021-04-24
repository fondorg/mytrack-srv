package ru.fondorg.mytracksrv;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fondorg.mytracksrv.domain.Project;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.service.ProjectService;

/**
 * Helper class to bootstrap project for tests
 */
@RequiredArgsConstructor
@Service
public class ProjectBootstrap {
    private final ProjectService projectService;


    public Project bootstrapProject(String title) {
        return bootstrapProject(title, null);
    }

    public Project bootstrapProject(String title, User user) {
        Project project = MytrackTestUtils.instanceOfProject(title);
        if (user == null) {
            user = MytrackTestUtils.instanceOfUser("111");
        }
        return projectService.createProject(project, user);
    }
}
