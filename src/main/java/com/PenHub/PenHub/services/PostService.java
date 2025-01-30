package com.PenHub.PenHub.services;

import com.PenHub.PenHub.dtos.PostDto.PostRequestDto;
import com.PenHub.PenHub.dtos.PostDto.PostResponseDto;
import com.PenHub.PenHub.enteties.Post;
import com.PenHub.PenHub.enteties.Tag;
import com.PenHub.PenHub.repositories.PostRepository;
import com.PenHub.PenHub.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
   final private PostRepository postRepository;

     public PostService(PostRepository postRepository, TagRepository tagRepository){
        this.postRepository = postRepository;
         this.tagRepository = tagRepository;
     }

    @Autowired
   final private TagRepository tagRepository;




    // Helper method to handle tags persistence logic
    private Set<Tag> getPersistedTags(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> tagRepository.findByName(tag.getName()).orElseGet(() -> tagRepository.save(tag)))
                .collect(Collectors.toSet());
    }
    // Create a post, associating tags and saving them
    public Post createPost(Post post) {
        post.setTags(getPersistedTags(post.getTags()));
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
    public Post update(int id, Post post) {
        getpost(id); // Ensure post exists
        post.setId(id);
        post.setTags(getPersistedTags(post.getTags()));
        return postRepository.save(post);
    }


    // Delete a post by ID
    public void  delete(int id) {
        Post post=postRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Post With "+id+ "Not Found"));
        post.setTags(new HashSet<>());
        postRepository.deleteById(id);
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
        return new PostResponseDto(post.getId(), post.getTitle(), post.getDescription(),
                post.getTags().stream().map(tag->tag.getName()).collect(Collectors.toSet()),post.getCreatedDate(), post.getLastModifiedDate());
    }


}
