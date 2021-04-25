package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.fondorg.mytracksrv.domain.Tag;
import ru.fondorg.mytracksrv.repo.IssueRepository;
import ru.fondorg.mytracksrv.repo.ProjectRepository;
import ru.fondorg.mytracksrv.repo.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final IssueRepository issueRepository;

    private final TagRepository tagRepository;

    private final ProjectRepository projectRepository;

    private final QueryService queryService;

    /**
     * Creates new tag
     *
     * @param tag    Tag object to persist
     * @param userId Current user id
     * @return
     */
    @PreAuthorize("#tag.project == null || @projectService.isUserParticipatesInProject(#tag.project.id, #userId)")
    public Tag saveTag(Tag tag, String userId) {
        return tagRepository.save(tag);
    }

    @PreAuthorize("isAuthenticated()")
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public Tag saveProjectTag(Tag tag, Long projectId, String userId) {
        projectRepository.findById(projectId).ifPresent(tag::setProject);
        return tagRepository.save(tag);
    }

    /**
     * Gets a list of tags for the specified project
     *
     * @param projectId Project id
     * @param userId    Current user
     * @return List of tags for specific project
     */
    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public List<Tag> getProjectTags(Long projectId, String userId) {
        return tagRepository.findByProjectIdOrProjectIsNull(projectId);
    }

    /**
     * Gets project tag with the specified id
     *
     * @param projectId project id
     * @param tagId     tag id
     * @param userId    current authenticated user id
     * @return Tag if found
     */
    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public Optional<Tag> getProjectTag(Long projectId, Long tagId, String userId) {
        return tagRepository.findById(tagId);
    }

    /**
     * Gets a list of tags that are common for all projects
     *
     * @return list of all common tags
     */
    @PreAuthorize("isAuthenticated()")
    public Page<Tag> getCommonTags(MultiValueMap<String, String> params) {
        return tagRepository.findByProjectIsNull(queryService.getPageable(params));
    }

    @PreAuthorize("isAuthenticated()")
    public Optional<Tag> getCommonTag(Long tagId) {
        return tagRepository.findById(tagId);

    }

    @PreAuthorize("#projectId == null || @projectService.isUserParticipatesInProject(#projectId, #userId)")
    public void deleteProjectTag(Long tagId, Long projectId, String userId) {
        tagRepository.deleteById(tagId);
    }

    @PreAuthorize("isAuthenticated()")
    public void deleteCommonTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }


    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public List<Tag> getIssueTags(Long projectId, Long issueId, String userId) {
        return issueRepository.findIssueTags(issueId);
    }
}
