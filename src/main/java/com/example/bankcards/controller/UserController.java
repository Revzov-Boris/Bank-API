package com.example.bankcards.controller;

import com.example.bankcards.dto.UserPutRequest;
import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Управление пользователями")
public class UserController {
    private final UserService userServie;

    @Autowired
    public UserController(UserService userServie) {
        this.userServie = userServie;
    }

    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public EntityModel<UserResponse> getUserById(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable int id) {
        return EntityModel.of(userServie.getUserById(id));
    }

    @Operation(summary = "Получить список пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    @GetMapping
    public PagedModel<EntityModel<UserResponse>> getAllUsers(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "6")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> usersPage = userServie.getAllUsers(pageable);
        PagedModel<EntityModel<UserResponse>> pagedModel = PagedModel.of(
                usersPage.stream().map(u -> {
                   EntityModel<UserResponse> entityModel = EntityModel.of(u);
                   entityModel.add(linkTo(methodOn(UserController.class)
                           .getUserById(u.getId()))
                           .withSelfRel());
                   return entityModel;
                }).toList(),
                new PagedModel.PageMetadata(usersPage.getSize(),
                                            usersPage.getNumber(),
                                            usersPage.getTotalElements(),
                                            usersPage.getTotalPages())
        );

        pagedModel.add(Link.of(String.format("/users?page=%d&size=%d", page, size)));
        if (usersPage.hasNext()) {
            pagedModel.add(linkTo(methodOn(UserController.class)
                    .getAllUsers(page + 1, size))
                    .withRel("next"));
        }
        if (usersPage.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(UserController.class)
                    .getAllUsers(page - 1, size))
                    .withRel("prev"));
        }

        return pagedModel;
    }

    @Operation(summary = "Создать пользователя")
    @ApiResponse(responseCode = "201", description = "Новый пользователь создан")
    @ApiResponse(responseCode = "400", description = "Ошибка при создании пользователя")
    @PostMapping
    public ResponseEntity<EntityModel<UserResponse>> createUser(@Valid @RequestBody UserRequest user) {
        UserResponse response = userServie.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response));
    }

    @Operation(summary = "Обновить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "400", description = "Ошибка при обновлении пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> updateUser(
            @Valid @RequestBody UserPutRequest user,
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable int id) {
        UserResponse userResponse = userServie.updateUser(user, id);
        return ResponseEntity.status(200).body(EntityModel.of(userResponse));
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь и все его карты удалены"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable int id) {
        userServie.deleteUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
