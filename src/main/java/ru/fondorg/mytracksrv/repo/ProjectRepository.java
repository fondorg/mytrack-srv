package ru.fondorg.mytracksrv.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.fondorg.mytracksrv.domain.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, java.lang.Long>, QuerydslPredicateExecutor<Project> {

    List<Project> findByOwner(String owner);

    Optional<Project> findByIdAndOwner(java.lang.Long id, String owner);

}
