package ru.fondorg.mytracksrv;

import ru.fondorg.mytracksrv.domain.Project;
import ru.fondorg.mytracksrv.domain.User;

import java.util.concurrent.atomic.AtomicInteger;

public class MytrackTestUtils {

    private static AtomicInteger userCounter = new AtomicInteger(1);

    private MytrackTestUtils() {
    }

    public static User instanceOfUser(String id) {
        User user = new User();
        user.setUsername(String.format("user%d", userCounter.get()));
        user.setFirstName(String.format("FirstName%d", userCounter.get()));
        user.setLastName(String.format("LastName%d", userCounter.get()));
        user.setId(id);
        userCounter.incrementAndGet();
        return user;
    }

    public static Project instanceOfProject() {
        return instanceOfProject("Test project");
    }

    public static Project instanceOfProject(String title) {
        Project project = new Project();
        project.setTitle(title);
        project.setDescription("test description");
        return project;
    }
}
