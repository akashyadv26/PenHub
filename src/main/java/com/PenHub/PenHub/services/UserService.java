package com.PenHub.PenHub.services;

import com.PenHub.PenHub.dtos.UserDto.UserPartialRequestDto;
import com.PenHub.PenHub.dtos.UserDto.UserRequestDto;
import com.PenHub.PenHub.dtos.UserDto.UserResponseDto;
import com.PenHub.PenHub.enteties.User;
import com.PenHub.PenHub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    final private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User create(User user){
         if (userRepository.findByusername(user.getUsername()).isPresent()){
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User with " +user.getUsername() + "already exist");

         }
         return userRepository.save(user);
     }

     public Page<User>getAll(int page,int size,String sortDirection,String sortBy){
         Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(sortDirection),sortBy));
         return userRepository.findAll(pageable);
     }

     public User getById(int id){
      return userRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User With " +id+ "is not found"));
     }

     public User update(int id,User user){
         User existingUser=getById(id);
         if (user.getUsername()!=null){
             existingUser.setUsername(user.getUsername());
         }
         if (user.getPassword()!=null){
             existingUser.setPassword(user.getPassword());
         }
         if(user.getEmail()!=null){
             existingUser.setEmail(user.getEmail());
         }
         return userRepository.save(existingUser);
     }

     public void delete(int id){
         getById(id);
         userRepository.deleteById(id);
     }

     public User convertToUser(UserRequestDto userRequestDto){
         User user=new User();
         user.setUsername(userRequestDto.getUsername());
         user.setPassword(userRequestDto.getPassword());
         user.setEmail(userRequestDto.getEmail());

         return user;

     }

    public User convertToUser(UserPartialRequestDto userPartialRequestDto){
        User user=new User();
        user.setUsername(userPartialRequestDto.getUsername());
        user.setPassword(userPartialRequestDto.getPassword());
        user.setEmail(userPartialRequestDto.getEmail());

        return user;

    }

     public UserResponseDto convertTouserResponseDto(User user){
         UserResponseDto userResponseDto=new UserResponseDto();
         userResponseDto.setId(user.getId());
         userResponseDto.setUsername(user.getUsername());
         userResponseDto.setPassword(user.getPassword());
         userResponseDto.setEmail(user.getEmail());
         userResponseDto.setCreatedDate(user.getCreatedDate());
         userResponseDto.setLastModifiedDate(user.getLastModifiedDate());
        return  userResponseDto;
     }





}
