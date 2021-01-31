package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fondorg.mytracksrv.domain.User;
import ru.fondorg.mytracksrv.repo.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByIdOrCreate(User user) {
        return userRepository.findById(user.getId()).orElse(userRepository.save(user));
    }
}
