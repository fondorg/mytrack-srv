package ru.fondorg.mytracksrv.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fondorg.mytracksrv.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findById(String id);
}
