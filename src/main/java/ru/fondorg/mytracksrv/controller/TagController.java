package ru.fondorg.mytracksrv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.fondorg.mytracksrv.domain.Tag;
import ru.fondorg.mytracksrv.service.ServletRequestAttributesService;
import ru.fondorg.mytracksrv.service.TagService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final ServletRequestAttributesService requestAttributesService;

    @PostMapping(ApiV1Paths.PROJECT_TAGS)
    public Tag newProjectTag(@PathVariable Long projectId, @RequestBody Tag tag, HttpServletRequest request) {
        return tagService.saveProjectTag(tag, projectId, requestAttributesService.getUserFromRequest(request).getId());
    }

    @GetMapping(ApiV1Paths.PROJECT_TAGS)
    public List<Tag> getProjectTags(@PathVariable Long projectId, HttpServletRequest request) {
        return tagService.getProjectTags(projectId, requestAttributesService.getUserFromRequest(request).getId());
    }


    @DeleteMapping(ApiV1Paths.PROJECT_TAG)
    public Tag getProjectTag(@PathVariable Long projectId, @PathVariable Long tagId, HttpServletRequest request) {

        return null;
    }
}
