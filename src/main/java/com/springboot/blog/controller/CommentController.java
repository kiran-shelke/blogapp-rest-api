package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(
        name = "CRUD REST APIs for Comment resource"
)
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    //create comment
    //API http://localhost:8080/api/posts/19/comments
    @Operation(
            summary = "Create Comment REST API",
            description = "It is used to save/stored comment into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable long postId,
                                                    @Valid @RequestBody CommentDto commentDto)
    {
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    // list all comments of particular post
    //API http://localhost:8080/api/posts/19/comments
    @Operation(
            summary = "List all comment REST API",
            description = "It is used to list/show all the comment of respective post from database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable long postId)
    {
      return commentService.getCommentByPostId(postId);
    }

    // get comment by commentID
    // API http://localhost:8080/api/posts/19/comments/1
    @Operation(
            summary = "REST API to Get comment by id",
            description = "It is used to get the comment of respective post by providing post and comment id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long postId, @PathVariable Long commentId)
    {
        CommentDto commentDto= commentService.getCommentById(postId,commentId);
        return new ResponseEntity<>(commentDto,HttpStatus.OK);
    }

    //update comment
    //API http://localhost:8080/api/posts/19/comments/1
    @Operation(
            summary = "REST API to update the comment",
            description = "It is used to update the comment of respective post by providing post and comment id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PutMapping("{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long postId,
                                                    @PathVariable Long commentId,
                                                    @Valid @RequestBody CommentDto commentDto)
    {
        CommentDto updatedComment=commentService.updateComment(postId, commentId, commentDto);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

    //delete comment
    //API http://localhost:8080/api/posts/19/comments/1
    @Operation(
            summary = "REST API to delete comment by id",
            description = "It is used to delete the comment of respective post by providing post and comment id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @DeleteMapping("{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId,@PathVariable Long commentId)
    {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("comment deleted successfully..!!",HttpStatus.OK);
    }
}
