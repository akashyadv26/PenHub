package com.PenHub.PenHub.dtos.PostDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    private String title;
    private String description;
    private Set<String> tags=new HashSet<>();
}
