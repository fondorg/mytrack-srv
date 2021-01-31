package ru.fondorg.mytracksrv.controller;

import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.domain.Project;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.exception.NotFoundException;
import ru.fondorg.mytracksrv.repo.ProjectProjection;
import ru.fondorg.mytracksrv.repo.ProjectRepository;
import ru.fondorg.mytracksrv.service.KeycloakService;
import ru.fondorg.mytracksrv.service.ProjectService;
import ru.fondorg.mytracksrv.service.ServletRequestAttributesService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepository projectRepository;

    private final KeycloakRestTemplate keycloakRestTemplate;

    private final KeycloakService keycloakService;

    private final ServletRequestAttributesService requestAttributesService;

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public Project getProject(@PathVariable Long id, HttpServletRequest request) {
        //return projectRepository.findByIdAndOwner(id, principal.getName()).orElseThrow(() -> new NotFoundException("Project not found"));
        User user = requestAttributesService.getUserFromRequest(request);
        return projectService.getProject(id, user.getId()).orElseThrow(() -> new NotFoundException("Project not found"));
    }

    @GetMapping
    public List<ProjectProjection> getAllProjects(HttpServletRequest request) {
        /*KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        return projectRepository.findByOwner(context.getToken().getSubject());*/
        User user = requestAttributesService.getUserFromRequest(request);
        return projectService.findUserProjects(user);
    }

    @PostMapping
    public Project addProject(@RequestBody Project project, HttpServletRequest request) {
        User user = requestAttributesService.getUserFromRequest(request);
        projectService.createProject(project, user);
        //projectRepository.save(project);
        return project;
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id, KeycloakPrincipal<KeycloakSecurityContext> principal) {
        Project p = projectRepository.findByIdAndOwner(id, principal.getName()).orElseThrow(() -> new NotFoundException("Project not found"));
        projectRepository.delete(p);
    }

    @GetMapping("/{id}/issues")
    public Page<Issue> getProjectIssues(@PathVariable Long id, @RequestParam int page, @RequestParam int size, HttpServletRequest request) {
        User user = requestAttributesService.getUserFromRequest(request);
        return projectService.getProjectIssues(id, user.getId(), page, size);
    }
}
