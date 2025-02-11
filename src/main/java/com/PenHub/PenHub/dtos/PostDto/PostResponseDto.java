package com.PenHub.PenHub.dtos.PostDto;

import com.PenHub.PenHub.dtos.UserDto.UserAutherResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private int id;
    private String title;
    private String description;
    private Set<String> tags;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private UserAutherResponseDto author;
    private String ImageUrl;
}
