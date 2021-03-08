package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.fondorg.mytracksrv.domain.Tag;
import ru.fondorg.mytracksrv.repo.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final IssueService issueService;

    private final TagRepository tagRepository;
    private final ProjectService projectService;

    /**
     * Creates new tag
     * @param tag Tag object to persist
     * @param userId Current user id
     * @return
     */
    @PreAuthorize("#tag.project == null || @projectService.isUserParticipatesInProject(#tag.project.id, #userId)")
    public Tag newTag(Tag tag, String userId) {
        return tagRepository.save(tag);
    }

    /**
     * Gets a list of tags for the specified project
     * @param projectId Project id
     * @param userId Current user
     * @return List of tags for specific project
     */
    @PreAuthorize("@projectService.isUserParticipatesInProject(#projectId, #userId)")
    public List<Tag> getProjectTags(Long projectId, String userId) {
        return tagRepository.findByProjectIdOrProjectIsNull(projectId);
    }

    /**
     * Gets a list of tags that are common for all projects
     *
     * @return list of all common tags
     */
    @PreAuthorize("isAuthenticated()")
    public Page<Tag> getCommonTags(int page, int size) {
        return tagRepository.findByProjectIsNull(PageRequest.of(page, size));
    }

    @PreAuthorize("#projectId == null || @projectService.isUserParticipatesInProject(#projectId, #userId)")
    public void deleteTag(Long tagId, Long projectId, String userId) {
        tagRepository.deleteById(tagId);
    }

}
