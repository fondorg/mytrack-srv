package ru.fondorg.mytracksrv;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.fondorg.mytracksrv.controller.ApiV1Paths;
import ru.fondorg.mytracksrv.domain.Tag;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//todo: integration tests of authenticated controllers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class IssueTagsWebTest {
//    @Autowired
//    private TestRestTemplate restTemplate;
    @Autowired
    private ProjectBootstrap projectBootstrap;
    private Long projectId;
    private static final String COLOR = "#fffffff";

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    @WithMockUser
    public void setupProject() {
    }

    @Test
    @WithMockUser
    public void saveProjectTag() throws Exception {
        projectId = projectBootstrap.bootstrapProject("Project 1").getId();
        mockMvc.perform(post(ApiV1Paths.PROJECT_TAGS, projectId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new Tag("Tag1", COLOR)))
        ).andExpect(status().isOk());
//        ResponseEntity<String> resp = restTemplate.postForEntity(ApiV1Paths.PROJECT_TAGS, new Tag("Tag1", COLOR), String.class, projectId);
//        Assertions.assertThat(restTemplate.postForObject(ApiV1Paths.PROJECT_TAGS, new Tag("Tag1", COLOR), Tag.class, projectId))
//                .isNotNull().isInstanceOf(Tag.class);
    }
}
