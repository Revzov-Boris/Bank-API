package com.example.bankcards.service;

import com.example.bankcards.dto.UserPutRequest;
import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUserById(int id);
    UserResponse createUser(UserRequest user);
    void deleteUser(int id);
    UserResponse updateUser(UserPutRequest user, int id);
    boolean hasUserCard(int userId, int cardId);
}
