package ru.fondorg.mytracksrv.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ru.fondorg.mytracksrv.domain.Issue;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.domain.dto.IssueDto;
import ru.fondorg.mytracksrv.service.IssueService;
import ru.fondorg.mytracksrv.service.ServletRequestAttributesService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/v1/issues")
public class IssueController {

    private final IssueService issueService;
    private final ServletRequestAttributesService requestAttributesService;
    private final ModelMapper modelMapper;

    @PostMapping(ApiV1Paths.PROJECT_ISSUES)
    public IssueDto newProjectIssue(@PathVariable Long id, @RequestBody Issue issue, HttpServletRequest request) {
//        return issueService.saveIssue(issue, id, requestAttributesService.getUserFromRequest(request));
        return modelMapper.map(issueService.saveIssue(issue, id,
                requestAttributesService.getUserFromRequest(request)), IssueDto.class);
    }

//    @PostMapping(ApiV1Paths.PROJECT_ISSUES)
//    public IssueDto updateCreateIssue(@PathVariable Long id, @RequestBody Issue issue, HttpServletRequest request) {
//        Issue reloaded = issueService.saveIssue(issue, id, requestAttributesService.getUserFromRequest(request));
//        return modelMapper.map(reloaded, IssueDto.class);
//    }

    @GetMapping("/api/v1/projects/{projectId}/issues/{issueId}")
    public IssueDto getProjectIssue(@PathVariable Long projectId, @PathVariable Long issueId, HttpServletRequest request) {
        return issueService.getProjectIssue(projectId, issueId, requestAttributesService.getUserFromRequest(request))
                .map(issue -> modelMapper.map(issue, IssueDto.class)).orElse(null); //todo: consider ResponseEntity
    }

    @GetMapping(ApiV1Paths.PROJECT_ISSUES)
    public Page<IssueDto> listProjectIssues(@PathVariable Long id,
                                            @RequestParam MultiValueMap<String, String> qParams,
                                            HttpServletRequest request) {
        User user = requestAttributesService.getUserFromRequest(request);
        Page<Issue> issues = issueService.getProjectIssues(id, user.getId(), qParams);
        return issues.map(issue -> modelMapper.map(issue, IssueDto.class));
    }


    @DeleteMapping("/api/v1/projects/{projectId}/issues/{issueId}")
    public void deleteIssue(@PathVariable Long projectId, @PathVariable Long issueId, HttpServletRequest request) {
        User user = requestAttributesService.getUserFromRequest(request);
        issueService.deleteIssue(projectId, user.getId(), issueId);
    }


//    @GetMapping(ApiV1Paths.PROJECT_ISSUES)
//    public Page<IssueDto> findProjectIssuesByTag(@PathVariable Long id,
//                                                 @RequestParam(required = false, defaultValue = "1") Integer page,
//                                                 @RequestParam(required = false, defaultValue = "20") Integer size,
//                                                 @RequestParam Long tagId,
//                                                 @RequestParam(required = false, defaultValue = "open") String scope,
//                                                 HttpServletRequest request) {
//        User user = requestAttributesService.getUserFromRequest(request);
//        Page<Issue> issues = issueService.findProjectIssuesByTag(id, user.getId(), tagId, page, size, scope);
//        return issues.map(issue -> modelMapper.map(issue, IssueDto.class));
//    }
}


