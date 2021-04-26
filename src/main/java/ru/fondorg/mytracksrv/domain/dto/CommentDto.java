package ru.fondorg.mytracksrv.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.fondorg.mytracksrv.domain.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private LocalDateTime created;

    private String text;

    private User author;

    private Long issueId;
}
