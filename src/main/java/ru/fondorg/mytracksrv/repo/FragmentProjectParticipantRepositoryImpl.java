package ru.fondorg.mytracksrv.repo;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Component;
import ru.fondorg.mytracksrv.domain.ProjectParticipant;
import ru.fondorg.mytracksrv.domain.QProjectParticipant;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FragmentProjectParticipantRepositoryImpl implements FragmentProjectParticipantRepository {

    private final QDslHelper helper;


    @Override
    public Page<ProjectProjectionImpl> findProjects(Predicate predicate, Pageable pageable) {
        Querydsl query = helper.getQueryDsl(ProjectParticipant.class);
        JPQLQuery<ProjectProjectionImpl> q = helper.getFactory().select(Projections.constructor(ProjectProjectionImpl.class,
                QProjectParticipant.projectParticipant.project)).from(QProjectParticipant.projectParticipant)
                .where(predicate);
        long total = q.fetchCount();
        List<ProjectProjectionImpl> pp = query.applyPagination(pageable, q).fetch();
        return new PageImpl<>(pp, pageable, total);
    }
}
