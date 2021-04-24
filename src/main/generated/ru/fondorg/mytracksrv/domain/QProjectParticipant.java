package ru.fondorg.mytracksrv.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProjectParticipant is a Querydsl query type for ProjectParticipant
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProjectParticipant extends EntityPathBase<ProjectParticipant> {

    private static final long serialVersionUID = 1097515970L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectParticipant projectParticipant = new QProjectParticipant("projectParticipant");

    public final QProjectParticipantKey id;

    public final QProject project;

    public final EnumPath<ParticipantRole> role = createEnum("role", ParticipantRole.class);

    public final QUser user;

    public QProjectParticipant(String variable) {
        this(ProjectParticipant.class, forVariable(variable), INITS);
    }

    public QProjectParticipant(Path<? extends ProjectParticipant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProjectParticipant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProjectParticipant(PathMetadata metadata, PathInits inits) {
        this(ProjectParticipant.class, metadata, inits);
    }

    public QProjectParticipant(Class<? extends ProjectParticipant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QProjectParticipantKey(forProperty("id")) : null;
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

