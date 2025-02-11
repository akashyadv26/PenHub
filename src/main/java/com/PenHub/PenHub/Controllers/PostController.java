package com.PenHub.PenHub.Controllers;

import com.PenHub.PenHub.dtos.PostDto.PostRequestDto;
import com.PenHub.PenHub.dtos.PostDto.PostResponseDto;
import com.PenHub.PenHub.enteties.Post;
import com.PenHub.PenHub.enteties.Tag;
import com.PenHub.PenHub.services.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {



//    It is Called Field injection

//    @Autowired
//    PostService postService;


//    It is called Construction Injection

    @Autowired
    final private PostService postService;
    final private ObjectMapper objectMapper;

    PostController(PostService postService, ObjectMapper objectMapper){
        this.postService=postService;
        this.objectMapper = objectMapper;
    }


    @GetMapping("")
    public ResponseEntity<Page<PostResponseDto>> getAllPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "id") String sortBy

    ){
        Page<PostResponseDto>posts=postService.getAll(page,size,sortDirection,sortBy).map(post -> postService.ConvertToPostResponse(post));
        return ResponseEntity.ok(posts);
    }

    @PostMapping(value = "/user/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDto> CreatePost(@RequestPart(name = "post",required = true) @Valid @NotNull String postRequestDtoString, @RequestPart(name = "image") MultipartFile image, @PathVariable int userId){
       PostRequestDto postRequestDto=null;

        try {
            postRequestDto=objectMapper.readValue(postRequestDtoString,PostRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Invalid post data");
        }
        Post createdPost=postService.createPost(postService.ConvertToPost(postRequestDto),image,userId);
        return ResponseEntity.status(201).body(postService.ConvertToPostResponse(createdPost));

    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDto>> searchPosts(@RequestParam String value){
        List<Post> searchResult=postService.search(value);
        List<PostResponseDto> posts=searchResult.stream().map(postService::ConvertToPostResponse).toList();
        return ResponseEntity.ok().body(posts);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> GetPostById(@PathVariable int id){
        return new ResponseEntity<PostResponseDto>( (postService.ConvertToPostResponse(postService.getpost(id))),HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<List<PostResponseDto>>getpostByTitle(@RequestParam String value){
        return  new ResponseEntity<List<PostResponseDto>>((postService.getPostByTitle(value).stream().map(post -> postService.ConvertToPostResponse(post)).collect(Collectors.toList())),HttpStatus.OK);

    }

    @GetMapping("/tag")
    public ResponseEntity<List<PostResponseDto>>getPostByTag(@RequestParam String value){
        return new ResponseEntity<List<PostResponseDto>>(postService.getPostByTag(value).stream().map(post -> postService.ConvertToPostResponse(post)).collect(Collectors.toList()),HttpStatus.OK);
    }

    @PutMapping("/{id}/user/{userId}")
    public  ResponseEntity<PostResponseDto> UpdatePostById(@PathVariable int id,@PathVariable int userId, @RequestBody PostRequestDto postRequestDto){

        Post updatedPost=postService.update(id,postService.ConvertToPost(postRequestDto),userId);
        return new ResponseEntity<PostResponseDto>(postService.ConvertToPostResponse(updatedPost),HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/user/{userId}")
    public ResponseEntity<PostResponseDto> UpdatePartialPostById(@PathVariable int id,@PathVariable int userId,@RequestBody PostRequestDto postRequestDto ){
       Post updatedPost=postService.update(id,postService.ConvertToPost(postRequestDto),userId);
        return ResponseEntity.ok(postService.ConvertToPostResponse(updatedPost));
    }

    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<?> deleteById(@PathVariable int id,@PathVariable int userId){
        postService.delete(id,userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
