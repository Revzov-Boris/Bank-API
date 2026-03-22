package com.example.bankcards.controller;

import com.example.bankcards.dto.UserPutRequest;
import com.example.bankcards.dto.UserRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.service.UserService;
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
public class UserController {
    private final UserService userServie;

    @Autowired
    public UserController(UserService userServie) {
        this.userServie = userServie;
    }


    @GetMapping("/{id}")
    public EntityModel<UserResponse> getUserById(@PathVariable int id) {
        return EntityModel.of(userServie.getUserById(id));
    }


    @GetMapping
    public PagedModel<EntityModel<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page,
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


    @PostMapping
    public ResponseEntity<EntityModel<UserResponse>> createUser(@Valid @RequestBody UserRequest user) {
        UserResponse response = userServie.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(response));
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserPutRequest user,
                                                   @PathVariable int id) {
        UserResponse userResponse = userServie.updateUser(user, id);
        return ResponseEntity.status(200).body(userResponse);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userServie.deleteUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
