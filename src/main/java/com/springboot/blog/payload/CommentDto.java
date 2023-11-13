package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Schema(
        description = "Information of CommentDto class"
)
@Data
public class CommentDto {
    private long id;

    @Schema(
            description = "Name of commentator"
    )
    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @Schema(
            description = "Email of commentator"
    )
    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;

    @Schema(
            description = "Comment description/body"
    )
    @NotEmpty()
    @Size(min = 10,message = "body contents should be min 10 character")
    private String body;
}
