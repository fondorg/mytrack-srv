package ru.fondorg.mytracksrv.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QProjectParticipantKey is a Querydsl query type for ProjectParticipantKey
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QProjectParticipantKey extends BeanPath<ProjectParticipantKey> {

    private static final long serialVersionUID = -1487686851L;

    public static final QProjectParticipantKey projectParticipantKey = new QProjectParticipantKey("projectParticipantKey");

    public final NumberPath<Long> projectId = createNumber("projectId", Long.class);

    public final StringPath userId = createString("userId");

    public QProjectParticipantKey(String variable) {
        super(ProjectParticipantKey.class, forVariable(variable));
    }

    public QProjectParticipantKey(Path<? extends ProjectParticipantKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProjectParticipantKey(PathMetadata metadata) {
        super(ProjectParticipantKey.class, metadata);
    }

}

