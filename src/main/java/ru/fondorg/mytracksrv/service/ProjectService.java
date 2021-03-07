package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.fondorg.mytracksrv.domain.*;
import ru.fondorg.mytracksrv.domain.Project;
import ru.fondorg.mytracksrv.exception.ModelDeleteException;
import ru.fondorg.mytracksrv.repo.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserService userService;

    private final UserRepository userRepository;

    private final ProjectParticipantRepository projectParticipantRepository;

    private final ServletRequestAttributesService requestAttributesService;

    private final ProjectPreDeleteHandler projectPreDeletionHandler;

    /**
     * Creates new project in the repository, adds specified user to the project participants
     *
     * @param project Project to save
     * @param creator the project's creator. This user will be added to the project participants as owner.
     * @return created Project instance
     */
    public Project createProject(Project project, User creator) {
        creator = userService.findByIdOrCreate(creator);
        project.setOwner(creator.getId());
        project = projectRepository.save(project);

        ProjectParticipant participant = new ProjectParticipant();
        participant.setId(new ProjectParticipantKey());
        participant.setProject(project);
        participant.setUser(creator);
        participant.setRole(ParticipantRole.OWNER);
        projectParticipantRepository.save(participant);
        return project;
    }

    /**
     * Adds new User(participant) to the existing project
     *
     * @param project Project where user will be added
     * @param user    user to add
     * @param role    role of the user in the project
     * @return Project instance
     */
    public Project addUserToProject(Project project, User user, ParticipantRole role) {
        //make sure user exists
        user = userService.findByIdOrCreate(user);

        project.getParticipants().add(new ProjectParticipant(project, user, role));
        return projectRepository.save(project);
    }

    /**
     * Checks if the project with the specified project id has a currently logged in user as a participant
     *
     * @param projectId project id
     * @param request   HttpServletRequest instance
     * @return true if user participates in project. false otherwise
     */
    public boolean isUserParticipatesInProject(Long projectId, HttpServletRequest request) {
        User user = requestAttributesService.getUserFromRequest(request);
        return isUserParticipatesInProject(projectId, user.getId());
    }

    /**
     * Checks if the project with the specified project id has a user as a participant
     *
     * @param projectId project id to check
     * @param userId    user id to check
     * @return true if user participates in project. false otherwise
     */
    public boolean isUserParticipatesInProject(Long projectId, String userId) {
        if (projectId == null) {
            return false;
        }
        return projectParticipantRepository.findById(new ProjectParticipantKey(userId, projectId))
                .map(pp -> pp.getProject().getId().equals(projectId)).orElse(Boolean.FALSE);
    }

    @Transactional
    public void removeUserFromProject(Project project, User user) {
        ProjectParticipant participant = new ProjectParticipant(project, user);
        project.removeParticipant(participant);
        user.removeParticipant(participant);
        projectRepository.save(project);
        userRepository.save(user);
    }

    /**
     * Find all user projects
     *
     * @param user the participant of the projects
     * @return list of projects where the specified user participates
     */
    public Page<ProjectProjection> findUserProjects(User user, int page, int size) {
        return projectParticipantRepository.findDistinctByUserOrderByProjectDesc(user, PageRequest.of(page, size));
    }


    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public Optional<Project> getProject(Long projectId, String userId) {
        return projectRepository.findById(projectId);
    }

    @Transactional
    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #user.id)")
    public void deleteProject(Long projectId, User user) {
        projectRepository.findById(projectId).ifPresent(project -> {
            removeUserFromProject(project, user);
            boolean preDeletions = projectPreDeletionHandler.handlePreDeletionActions(projectId, user.getId());
            if (preDeletions) {
                projectRepository.deleteById(projectId);
            } else {
                throw new ModelDeleteException(String.format("Failed to delete project with id %d", projectId));
            }
        });
    }
}
