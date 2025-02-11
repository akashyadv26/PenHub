package com.PenHub.PenHub.services;

import com.PenHub.PenHub.dtos.PostDto.PostRequestDto;
import com.PenHub.PenHub.dtos.PostDto.PostResponseDto;
import com.PenHub.PenHub.dtos.UserDto.UserAutherResponseDto;
import com.PenHub.PenHub.enteties.Post;
import com.PenHub.PenHub.enteties.Tag;
import com.PenHub.PenHub.enteties.User;
import com.PenHub.PenHub.repositories.PostRepository;
import com.PenHub.PenHub.repositories.TagRepository;
import com.PenHub.PenHub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    final private PostRepository postRepository;
    final private TagRepository tagRepository;
    final private UserService userService;
    final private UserRepository userRepository;


    @Autowired
    ImageService imageService;

     public PostService(PostRepository postRepository, UserService userService, TagRepository tagRepository, UserRepository userRepository){
        this.postRepository = postRepository;
         this.userService = userService;
         this.tagRepository = tagRepository;
         this.userRepository = userRepository;
     }






    // Helper method to handle tags persistence logic
    private Set<Tag> getPersistedTags(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> tagRepository.findByName(tag.getName()).orElseGet(() -> tagRepository.save(tag)))
                .collect(Collectors.toSet());
    }
    // Create a post, associating tags and saving them
    public Post createPost(Post post, MultipartFile image, int userId) {
        User user=userService.getById(userId);
        post.setTags(getPersistedTags(post.getTags()));
        post.setUser(user);
        try {
            post.setImageUrl(imageService.saveImage(image));
        } catch (IOException |NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed To upload");
        }
        return postRepository.save(post);
    }


    // Retrieve all posts
    public Page<Post> getAll(int page,int size,String sortDirection,String sortBy){
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.fromString(sortDirection),sortBy));
        return postRepository.findAll(pageable);
    }

    public List<Post>search(String value){
        return postRepository.searchPosts(value);
    }

    // Get a single post by ID
    public Post getpost(int id){

        return postRepository.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User With " +id+ "Not Found")
        );
    }

    // Get posts by title (partial match)
    public List<Post> getPostByTitle(String title){
        // this is used for customize query we have used sql Query
       // return postRepository.findByTitleNative(title);
        // this is used for customize query we have used Jpql Query
        return postRepository.findByTitleContaining(title);
    }


    // Get posts by tag name
    public List<Post>getPostByTag(String tagname){
        return postRepository.findByTagsName(tagname);
    }

    // Update a post by ID
    public Post update(int id, Post post,int userid) {
        User user=userService.getById(userid);
        Post existingpost=user.getPosts().stream().filter(dbuser->dbuser.getId()==id).findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Post with id"+id+"not found"));

        if(post.getTitle()!=null){
            existingpost.setTitle(post.getTitle());
        }
        if (post.getDescription()!=null){
            existingpost.setDescription(post.getDescription());
        }
        if (post.getTags()!=null){
            existingpost.setTags(post.getTags());
        }

        return postRepository.save(existingpost);
    }


    // Delete a post by ID
    public void  delete(int id,int userId) {
         User user=userService.getById(userId);
         Post existingPost=user.getPosts().stream().filter(post -> post.getId()==id).findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Post With ID "+id+ "Not Found"));
         user.getPosts().removeIf(post -> post.getId()==id);


        existingPost.setTags(new HashSet<>());
        postRepository.delete(existingPost);
        userRepository.save(user);
    }

    // Convert PostRequestDto to Post entity
    public Post ConvertToPost(PostRequestDto postRequestDto){

        Post post=new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setDescription(postRequestDto.getDescription());
        post.setTags(postRequestDto.getTags().stream().map(tag->new Tag(tag.toUpperCase())).collect(Collectors.toSet()));

        return post;
    }


    // Convert Post entity to PostResponseDto
    public PostResponseDto ConvertToPostResponse(Post post){

        PostResponseDto postResponseDto=new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setDescription(post.getDescription());
        postResponseDto.setCreatedDate(post.getCreatedDate());
        postResponseDto.setLastModifiedDate(post.getLastModifiedDate());
        postResponseDto.setTags(post.getTags().stream().map(tag->tag.getName()).collect(Collectors.toSet()));
        postResponseDto.setAuthor(convertTouserAutherResponseDto(post.getUser()));
        postResponseDto.setImageUrl(post.getImageUrl());
        return postResponseDto;
    }

    public UserAutherResponseDto convertTouserAutherResponseDto(User user){
         UserAutherResponseDto userAutherResponseDto=new UserAutherResponseDto();
         userAutherResponseDto.setId(user.getId());
         userAutherResponseDto.setUsername(user.getUsername());
         return userAutherResponseDto;
    }


}
