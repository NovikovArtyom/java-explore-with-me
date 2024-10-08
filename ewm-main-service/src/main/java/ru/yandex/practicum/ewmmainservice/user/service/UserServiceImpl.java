package ru.yandex.practicum.ewmmainservice.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainservice.exception.DataIntegrityViolationException;
import ru.yandex.practicum.ewmmainservice.exception.UserNotFoundException;
import ru.yandex.practicum.ewmmainservice.user.model.UserEntity;
import ru.yandex.practicum.ewmmainservice.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserEntity> findAllUsers(Integer from, Integer size, List<Long> usersIds) {
        log.info("User. Service: 'findAllUsers' method called");
        if (usersIds == null) {
            return userRepository.findAll(PageRequest.of(from, size));
        } else {
            return userRepository.findAllByIdIn(usersIds, PageRequest.of(from, size));
        }
    }

    @Override
    @Transactional
    public UserEntity addUser(UserEntity userEntity) {
        log.info("User. Service: 'addUser' method called");
        try {
            return userRepository.save(userEntity);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("User. Service: 'deleteUser' method called");
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findUserById(Long userId) {
        log.info("User. Service: 'findUserById' method called");
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
