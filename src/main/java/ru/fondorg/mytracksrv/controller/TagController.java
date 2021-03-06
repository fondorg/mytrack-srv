package ru.fondorg.mytracksrv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ru.fondorg.mytracksrv.domain.Tag;
import ru.fondorg.mytracksrv.service.ServletRequestAttributesService;
import ru.fondorg.mytracksrv.service.TagService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final ServletRequestAttributesService requestAttributesService;

    @PostMapping(ApiV1Paths.PROJECT_TAGS)
    public Tag newProjectTag(@PathVariable Long projectId, @RequestBody Tag tag, HttpServletRequest request) {
        return tagService.saveProjectTag(tag, projectId, requestAttributesService.getUserFromRequest(request).getId());
    }

    @PostMapping(ApiV1Paths.TAGS)
    public Tag newCommonTag(@RequestBody Tag tag) {
        return tagService.saveTag(tag);
    }

    @GetMapping(ApiV1Paths.PROJECT_TAGS)
    public List<Tag> getProjectTags(@PathVariable Long projectId, HttpServletRequest request) {
        return tagService.getProjectTags(projectId, requestAttributesService.getUserFromRequest(request).getId());
    }

    @GetMapping(ApiV1Paths.TAGS)
    public Page<Tag> getCommonTags(@RequestParam MultiValueMap<String, String> params) {
        return tagService.getCommonTags(params);
    }

    @GetMapping(ApiV1Paths.PROJECT_TAG)
    public Tag getProjectTag(@PathVariable Long projectId, @PathVariable Long tagId, HttpServletRequest request) {
        return tagService.getProjectTag(projectId, tagId, requestAttributesService.getUserFromRequest(request).getId()).orElse(null);
    }

    @GetMapping(ApiV1Paths.TAG)
    public Tag getCommonTag(@PathVariable Long tagId) {
        return tagService.getCommonTag(tagId).orElse(null);
    }

    @DeleteMapping(ApiV1Paths.PROJECT_TAG)
    public void deleteProjectTag(@PathVariable Long projectId, @PathVariable Long tagId, HttpServletRequest request) {
        tagService.deleteProjectTag(tagId, projectId, requestAttributesService.getUserFromRequest(request).getId());
    }

    @DeleteMapping(ApiV1Paths.TAG)
    public void deleteCommonTag(@PathVariable Long tagId) {
        tagService.deleteCommonTag(tagId);
    }


    @GetMapping(ApiV1Paths.ISSUE_TAGS)
    public List<Tag> getIssueTags(@PathVariable Long projectId, @PathVariable Long issueId, HttpServletRequest request) {
        return tagService.getIssueTags(projectId, issueId, requestAttributesService.getUserFromRequest(request).getId());
    }
}
