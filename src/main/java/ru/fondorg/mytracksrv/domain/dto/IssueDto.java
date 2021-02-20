package ru.fondorg.mytracksrv.domain.dto;

import lombok.Data;
import ru.fondorg.mytracksrv.domain.User;

@Data
public class IssueDto {

    private Long id;
    private String title;
    private String description;
    private Long projectId;
    private User author;
    private Boolean closed;
}
