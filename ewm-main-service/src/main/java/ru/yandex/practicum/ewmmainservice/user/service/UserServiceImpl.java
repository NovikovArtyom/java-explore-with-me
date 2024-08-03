package ru.yandex.practicum.ewmmainservice.user.service;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainservice.exception.DataIntegrityViolationException;
import ru.yandex.practicum.ewmmainservice.exception.UserNotFoundException;
import ru.yandex.practicum.ewmmainservice.user.model.UserEntity;
import ru.yandex.practicum.ewmmainservice.user.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO: Запрос составлен некорректно
    @Override
    @Transactional(readOnly = true)
    public Page<UserEntity> findAllUsers(Integer from, Integer size, List<Long> usersIds) {
        if (usersIds.isEmpty()) {
            return userRepository.findAll(PageRequest.of(from, size));
        } else {
            return userRepository.findAllByIdIn(usersIds, PageRequest.of(from, size));
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserEntity addUser(UserEntity userEntity) {
        try {
            return userRepository.save(userEntity);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
