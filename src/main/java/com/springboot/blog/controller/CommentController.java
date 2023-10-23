package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    //create comment
    //API http://localhost:8080/api/posts/19/comments
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable long postId,
                                                    @Valid @RequestBody CommentDto commentDto)
    {
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    // list all comments of particular post
    //API http://localhost:8080/api/posts/19/comments
    @GetMapping("/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable long postId)
    {
      return commentService.getCommentByPostId(postId);
    }

    // get comment by commentID
    // API http://localhost:8080/api/posts/19/comments/1
    @GetMapping("{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long postId, @PathVariable Long commentId)
    {
        CommentDto commentDto= commentService.getCommentById(postId,commentId);
        return new ResponseEntity<>(commentDto,HttpStatus.OK);
    }

    //update comment
    //API http://localhost:8080/api/posts/19/comments/1
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
    @DeleteMapping("{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId,@PathVariable Long commentId)
    {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("comment deleted successfully..!!",HttpStatus.OK);
    }
}
