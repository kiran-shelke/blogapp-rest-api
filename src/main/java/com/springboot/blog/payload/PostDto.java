package com.springboot.blog.payload;

import com.springboot.blog.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;
@Schema(
        description = "PostDto model class information "
)
@Data
public class PostDto {
    private long id;
    //Adding validation for required fields
    @Schema(
            description = "Title of Post"
    )
    @NotEmpty
    @Size(min = 2,message = "Title should be of min 2 character")
    private String title;

    @Schema(
            description = "Description of Post"
    )
    @NotEmpty
    @Size(min = 10,message = "Description should be min of 10 character")
    private  String description;

    @Schema(
            description = "Content of Post"
    )
    @NotEmpty
    private String content;

    @Schema(
            description = "List of comments of Post"
    )
    private Set<CommentDto> comments; //This filed use to include comments in post api call, variable names should be similar to Post entity

    private Long categoryId;
}
