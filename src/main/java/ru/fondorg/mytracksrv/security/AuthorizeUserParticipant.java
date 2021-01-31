package ru.fondorg.mytracksrv.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@PreAuthorize("@projectService.isUserParticipatesInProject(#issue.project.id, #user.id)")
public @interface AuthorizeUserParticipant {
}
