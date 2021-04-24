package ru.fondorg.mytracksrv.repo;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FragmentProjectParticipantRepository {
    Page<ProjectProjectionImpl> findProjects(Predicate predicate, Pageable pageable);
}
