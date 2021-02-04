package ru.fondorg.mytracksrv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.service.IssueService;
import ru.fondorg.mytracksrv.service.ServletRequestAttributesService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/issues")
public class IssueController {

    private final IssueService issueService;
    private final ServletRequestAttributesService requestAttributesService;

    @PostMapping
    public Issue addIssue(@RequestBody Issue issue, Long projectId, HttpServletRequest request) {
        return issueService.saveIssue(issue, projectId, requestAttributesService.getUserFromRequest(request));
    }

    @GetMapping("/{id}")
    public Issue getIssue(@PathVariable Long issueId, HttpServletRequest request) {
        return null;
    }
}
