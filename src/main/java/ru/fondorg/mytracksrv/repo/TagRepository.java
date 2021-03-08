package ru.fondorg.mytracksrv.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.fondorg.mytracksrv.domain.Tag;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByProjectIdOrProjectIsNull(Long projectId);

    Page<Tag> findByProjectIsNull(Pageable pageable);
}
