package com.PenHub.PenHub.Controllers;

import com.PenHub.PenHub.dtos.UserDto.UserPartialRequestDto;
import com.PenHub.PenHub.dtos.UserDto.UserRequestDto;
import com.PenHub.PenHub.dtos.UserDto.UserResponseDto;
import com.PenHub.PenHub.enteties.User;
import com.PenHub.PenHub.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<Page<UserResponseDto>>getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "id") String sortBy
    ){
      Page<UserResponseDto>  allUser=userService.getAll(page,size,sortDirection,sortBy).map(user -> userService.convertTouserResponseDto(user));
          return ResponseEntity.ok(allUser);
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto>createUser(@Valid @RequestBody UserRequestDto userRequestDto){
       User user= userService.convertToUser(userRequestDto);
        User createdUser = userService.create(user);
        UserResponseDto userResponseDto=userService.convertTouserResponseDto(createdUser);
        return ResponseEntity.status(201).body(userResponseDto);

    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDto>getById(@PathVariable int id){
        User user=userService.getById(id);
        UserResponseDto userResponseDto=userService.convertTouserResponseDto(user);
        return ResponseEntity.ok(userResponseDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void>deleteUserById(@PathVariable int id){
        userService.delete(id);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("{id}")
    public ResponseEntity<UserResponseDto>updateUserById(@PathVariable int id, @Valid @RequestBody UserPartialRequestDto userPartialRequestDto){
        User user=userService.convertToUser(userPartialRequestDto);
        User updatedUser=userService.update(id,user);
        UserResponseDto userResponseDto=userService.convertTouserResponseDto(updatedUser);
        return ResponseEntity.ok(userResponseDto);
    }


}
