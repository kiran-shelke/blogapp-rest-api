package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.util.AppConstant;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create post api- http://localhost:8080/api/posts
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto)
    {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get all post api- http://localhost:8080/api/posts
    //                  http://localhost:8080/api/posts?pageNo=2&pageSize=5
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
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name="id") long id)
    {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    //update post by id- http://localhost:8080/api/posts/1
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable long id)
    {
       return new ResponseEntity<>(postService.updatePost(postDto,id),HttpStatus.OK);
    }

    //delete post by id- http://localhost:8080/api/posts/1
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable long id)
    {
      postService.deletePostById(id);
      return new ResponseEntity<>("post deleted successfully..!", HttpStatus.OK);
    }

    //get posts by category rest api- http://localhost:8080/api/posts/category/1
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable("id") Long categoryId)
    {
        List<PostDto> postDtos=postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(postDtos);
    }
}
