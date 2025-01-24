package com.PenHub.PenHub.Controllers;

import com.PenHub.PenHub.dtos.postDto.PostRequestDto;
import com.PenHub.PenHub.dtos.postDto.PostResponseDto;
import com.PenHub.PenHub.enteties.Post;
import com.PenHub.PenHub.enteties.Tag;
import com.PenHub.PenHub.services.PostService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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

    PostController(PostService postService){
        this.postService=postService;
    }



    @GetMapping("")
    public ResponseEntity<List<PostResponseDto>> getAllPost(){
        return new ResponseEntity<List<PostResponseDto>>(postService.getAll().stream().map(post -> postService.ConvertToPostResponse(post)).collect(Collectors.toList()),HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<PostResponseDto> CreatePost(@RequestBody PostRequestDto postRequestDto){
        Post postResponse=postService.createPost(postService.ConvertToPost(postRequestDto));
        return new ResponseEntity<PostResponseDto>(postService.ConvertToPostResponse(postResponse), HttpStatus.CREATED);

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

    @PutMapping("/{id}")
    public  ResponseEntity<PostResponseDto> UpdatePostById(@PathVariable int id, @RequestBody PostRequestDto postRequestDto){

        Post postResponse=postService.update(id,postService.ConvertToPost(postRequestDto));
        return new ResponseEntity<PostResponseDto>(postService.ConvertToPostResponse(postResponse),HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponseDto> UpdatePartialPostById(@PathVariable int id,@RequestBody PostRequestDto postRequestDto ){
        Post oldpost=postService.getpost(id);
        oldpost.setTitle(postRequestDto.getTitle() !=null ? postRequestDto.getTitle():oldpost.getTitle());
        oldpost.setDescription(postRequestDto.getDescription() !=null ? postRequestDto.getTitle() :oldpost.getDescription());
        oldpost.setTags(!postRequestDto.getTags().isEmpty()?postRequestDto.getTags().stream().map(name ->new Tag(name)).collect(Collectors.toSet()):oldpost.getTags());
        Post postResponse=postService.update(id,oldpost);
        return new ResponseEntity<PostResponseDto>(postService.ConvertToPostResponse(postResponse),HttpStatus.CREATED) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id){
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
