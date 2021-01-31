package ru.fondorg.mytracksrv.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fondorg.mytracksrv.domain.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwner(String owner);

    Optional<Project> findByIdAndOwner(Long id, String owner);

}
