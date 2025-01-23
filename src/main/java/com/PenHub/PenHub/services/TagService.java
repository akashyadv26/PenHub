package com.PenHub.PenHub.services;


import com.PenHub.PenHub.dtos.postDto.TagRequestDto;
import com.PenHub.PenHub.dtos.postDto.TagResponseDto;
import com.PenHub.PenHub.enteties.Tag;
import com.PenHub.PenHub.repositories.PostRepository;
import com.PenHub.PenHub.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TagService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    TagRepository tagRepository;

    public List<Tag>getAll(){
        return tagRepository.findAll();
    }

    public Tag create(Tag tag){
        if (tagRepository.findByName(tag.getName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"tag with name: "+tag.getName()+ "alredy exist you cannot create duplicate tags");

        }
        return tagRepository.save(tag);
    }

    public Tag update(String oldname ,Tag tag){
        String newTagName=tag.getName();
        if (oldname.equals(newTagName)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"you cannot update same tag name");
        }
        Tag existingTag= tagRepository.findByName(oldname).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Tag with name: "+oldname+"not found"));

        if (tagRepository.findByName(newTagName).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"you cannot update from "+oldname+ "to "+newTagName+ " because "+newTagName+ "is already exist in database");

        }
        existingTag.setName(newTagName);
        return tagRepository.save(existingTag);
    }

    public void delete(String tagName){
       Tag tag= tagRepository.findByName(tagName).orElseThrow(()->
               new ResponseStatusException(HttpStatus.NOT_FOUND,"tag with name :" +tagName+ "not found"));
        if (!postRepository.findByTagsName(tagName).isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"you cannot delete tag name: "+tagName+ "because there are some posts who are using its refrences");

        }
        tagRepository.delete(tag);
    }

    public Tag convertToTag(TagRequestDto tagRequestDto){
       return new Tag(0,tagRequestDto.getName());
    }

    public TagResponseDto convertToTagResponse(Tag tag){
        return new TagResponseDto(tag.getId(), tag.getName());
    }
}
