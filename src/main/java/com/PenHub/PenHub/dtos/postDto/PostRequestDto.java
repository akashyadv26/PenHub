package com.PenHub.PenHub.dtos.postDto;

import com.PenHub.PenHub.enteties.Tag;
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
    private String Description;
    private Set<String> tags=new HashSet<>();
}
