package com.PenHub.PenHub.services;

import com.PenHub.PenHub.dtos.postDto.PostRequestDto;
import com.PenHub.PenHub.dtos.postDto.PostResponseDto;
import com.PenHub.PenHub.enteties.Post;
import com.PenHub.PenHub.enteties.Tag;
import com.PenHub.PenHub.repositories.PostRepository;
import com.PenHub.PenHub.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    //Added Student Record In LinkedHasMAp Because We are Not Using Database
    //Has Map Store Value In key And Object Form


//    LinkedHashMap<Integer, Post> posts=new LinkedHashMap<>(
//            Map.of(1,new Post(1,"Java","Java Is Oops Based programming language"),
//                    2,new Post(2,"Python","Python is high level language"))
//    );

    @Autowired
   final private PostRepository postRepository;

     public PostService(PostRepository postRepository, TagRepository tagRepository){
        this.postRepository = postRepository;
         this.tagRepository = tagRepository;
     }

    @Autowired
   final private TagRepository tagRepository;





    public Post createPost(Post post){
//         Set<Tag>persistedTags=new HashSet<>();
//         for (Tag tag: post.getTags()){
//             Tag persistedTag=tagRepository.findByName(tag.getName());
//             if(persistedTag == null){
//                 persistedTag =tagRepository.save(tag);
//             }
//             persistedTags.add(persistedTag);
//         }

        Set<Tag>persistedTags=post.getTags().stream().map(tag -> {
            Tag persistedTag=tagRepository.findByName(tag.getName());
            return persistedTag ==null ?tagRepository.save(tag):persistedTag;
        }).collect(Collectors.toSet());
         post.setTags(persistedTags);
        return postRepository.save(post);
    }

    public List<Post> getAll(){
        return postRepository.findAll();
    }

    public Post getpost(int id){

        return postRepository.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User With " +id+ "Not Found")
        );
    }

    public List<Post> getPostByTitle(String title){
       //        return postRepository.findByTitle(title);

        // this is used for customize query we have used sql Query
       // return postRepository.findByTitleNative(title);

        // this is used for customize query we have used Jpql Query
        return postRepository.findByTitleContaining(title);
    }

    public List<Post>getPostByTag(String tagname){
        return postRepository.findByTagsName(tagname);
    }

    public Post Update(int id,Post post){
        getpost(id);
        post.setId(id);
        Set<Tag>persistedTags=post.getTags().stream().map(tag -> {
            Tag persistedTag=tagRepository.findByName(tag.getName());
            return persistedTag ==null ?tagRepository.save(tag):persistedTag;
        }).collect(Collectors.toSet());
        post.setTags(persistedTags);

        return postRepository.save(post);
    }

    public void  delete(int id) {
        Post post=postRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Post With "+id+ "Not Found"));
        post.setTags(new HashSet<>());
        postRepository.deleteById(id);
    }

    public Post ConvertToPost(PostRequestDto postRequestDto){

        return new Post(0, postRequestDto.getTitle(), postRequestDto.getDescription(),
                postRequestDto.getTags().stream().map(tag->new Tag(tag.toUpperCase())).collect(Collectors.toSet()));
    }

    public PostResponseDto ConvertToPostResponse(Post post){
        return new PostResponseDto(post.getId(), post.getTitle(), post.getDescription(),post.getTags().stream().map(tag->tag.getName()).collect(Collectors.toSet()));
    }


}
