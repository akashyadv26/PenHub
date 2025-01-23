package com.PenHub.PenHub.dtos.postDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private int id;
    private String title;
    private String Description;
    private Set<String> tags;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;


}
