package com.springboot.blog.payload;

import com.springboot.blog.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private long id;
    //Adding validation for required fields
    @NotEmpty
    @Size(min = 2,message = "Title should be of min 2 character")
    private String title;

    @NotEmpty
    @Size(min = 10,message = "Description should be min of 10 character")
    private  String description;

    @NotEmpty
    private String content;
    private Set<CommentDto> comments; //This filed use to include comments in post api call, variable names should be similar to Post entity

    private Long categoryId;
}
