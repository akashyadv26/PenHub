package com.PenHub.PenHub.Controllers;


import com.PenHub.PenHub.dtos.postDto.TagRequestDto;
import com.PenHub.PenHub.dtos.postDto.TagResponseDto;
import com.PenHub.PenHub.enteties.Tag;
import com.PenHub.PenHub.repositories.TagRepository;
import com.PenHub.PenHub.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    TagService tagService;


    @GetMapping()
    public ResponseEntity<List<TagResponseDto>>getAllTags(){
        return new ResponseEntity<>(tagService.getAll().stream().map(tag -> tagService.convertToTagResponse(tag)).collect(Collectors.toList()),HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<TagResponseDto>createTag(@RequestBody TagRequestDto tagRequestDto){
      Tag tag=tagService.convertToTag(tagRequestDto);
      Tag createdTag=tagService.create(tag);
      return new ResponseEntity<TagResponseDto>(tagService.convertToTagResponse(createdTag),HttpStatus.CREATED);
    }

    @PutMapping("/{tagname}")
    public ResponseEntity<TagResponseDto>updateTag(@PathVariable  String tagname,@RequestBody TagRequestDto tagRequestDto){
        Tag tag=tagService.convertToTag(tagRequestDto);
        Tag updatedTag=tagService.update(tagname,tag);
        return new ResponseEntity<TagResponseDto>(tagService.convertToTagResponse(updatedTag),HttpStatus.OK);
    }

    @DeleteMapping("/{tagname}")
    public ResponseEntity<?>deleteTag(@PathVariable String tagname){
        tagService.delete(tagname);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
