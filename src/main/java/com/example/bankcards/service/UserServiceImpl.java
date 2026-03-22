package com.example.bankcards.service;

import com.example.bankcards.dto.UserPutRequest;
import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.NonUniqueUserLoginException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAllByRole(pageable, Role.USER).map(e -> toResponse(e));
    }


    @Override
    public UserResponse getUserById(int id) {
        return toResponse(userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id))
        );
    }

    public UserResponse toResponse(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .secondName(entity.getSecondName())
                .thirdName(entity.getThirdName())
                .birthDate(entity.getBirthDate())
                .build();
    }


    @Override
    @Transactional
    public UserResponse createUser(@Valid @RequestBody UserRequest user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new NonUniqueUserLoginException("Попытка создать пользователя с существующим логином");
        }

        UserEntity userEntity = UserEntity.builder()
                .login(user.getLogin())
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .birthDate(user.getBirthDate())
                .role(Role.USER)
                .passHash(passwordEncoder.encode(user.getPassword()))
                .build();
        userEntity = userRepository.save(userEntity);
        return toResponse(userEntity);
    }


    @Override
    @Transactional
    public void deleteUser(int id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }


    @Override
    @Transactional
    public UserResponse updateUser(@Valid @RequestBody UserPutRequest user, int id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );
        userEntity.setFirstName(user.getFirstName());
        userEntity.setSecondName(user.getSecondName());
        userEntity.setThirdName(user.getThirdName());
        userEntity.setBirthDate(user.getBirthDate());
        userEntity = userRepository.save(userEntity);
        return toResponse(userEntity);
    }
}
