package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.util.AppConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(
        name = "CRUD REST APIs for Post resource"
)
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create post api- http://localhost:8080/api/posts
    @Operation(
            summary = "Create Post REST API",
            description = "It is used to create/save post into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto)
    {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get all post api- http://localhost:8080/api/posts
    //                  http://localhost:8080/api/posts?pageNo=2&pageSize=5
    @Operation(
            summary = "Get All Post's REST API",
            description = "It is used to get all posts from database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo",defaultValue = AppConstant.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value="pageSize",defaultValue = AppConstant.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue=AppConstant.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortOrder",defaultValue = AppConstant.DEFAULT_SORT_ORDER,required = false) String sortOrder)
    {
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortOrder);
    }

    //get post by id api- http://localhost:8080/api/posts/1
    @Operation(
            summary = "Get Post by id REST API",
            description = "It is used to get a post into databse"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name="id") long id)
    {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    //update post by id- http://localhost:8080/api/posts/1
    @Operation(
            summary = "Update Post REST API",
            description = "It is used to update the post into database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable long id)
    {
       return new ResponseEntity<>(postService.updatePost(postDto,id),HttpStatus.OK);
    }

    //delete post by id- http://localhost:8080/api/posts/1
    @Operation(
            summary = "Delete Post REST API",
            description = "It is used to delete the post from database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable long id)
    {
      postService.deletePostById(id);
      return new ResponseEntity<>("post deleted successfully..!", HttpStatus.OK);
    }

    //get posts by category rest api- http://localhost:8080/api/posts/category/1
    @Operation(
            summary = "Get Posts by Category REST API",
            description = "It is used to get all post from database according to category"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable("id") Long categoryId)
    {
        List<PostDto> postDtos=postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(postDtos);
    }
}
