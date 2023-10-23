package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private ModelMapper mapper;
    private CategoryRepository categoryRepository;
    public PostServiceImpl(PostRepository postRepository,ModelMapper mapper,CategoryRepository categoryRepository) {

        this.postRepository = postRepository;
        this.mapper=mapper;
        this.categoryRepository=categoryRepository;
    }

    //create post
    @Override
    public PostDto createPost(PostDto postDto) {
        Category category=categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category","id",postDto.getCategoryId()));

        Post post=dtoToEntity(postDto);
        post.setCategory(category);
        Post newPost=postRepository.save(post);
        PostDto postResponse=entityToDto(newPost);
        return postResponse;
    }

    //display all post
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortOrder){
        Sort sort=sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //creating Pageable object
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        //calling findAll method which takes pageable obj as parameter and return the Page obj
        Page<Post> page=postRepository.findAll(pageable);
        //convert page into list by using getContent method of page interface
        List<Post> posts=page.getContent();
        //convert each Post entity into DTO and store into a list
        List<PostDto> listOfPostDto= posts.stream().map(post->entityToDto(post)).collect(Collectors.toList());
        //create object of PostResponse
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(listOfPostDto);
        postResponse.setPageNo(page.getNumber());
        postResponse.setPageSize(page.getSize());
        postResponse.setTotalPages(page.getTotalPages());
        postResponse.setTotalElements(page.getTotalElements());
        postResponse.setLastPage(page.isLast());

        return postResponse;
    }

    //get single post by id
    @Override
    public PostDto getPostById(long id)
    {
       Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        return entityToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        //get post by id from database
        Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));

        Category category=categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category","id",postDto.getCategoryId()));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);

       Post updatedPost= postRepository.save(post);

       //convert Post entity into PostDto DTO and return it
        return entityToDto(updatedPost);
    }

    //Delete post by Id
    @Override
    public void deletePostById(long id) {
        Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","id",categoryId));

        List<Post> posts=postRepository.findByCategoryId(categoryId);
        return posts.stream().map((post)->mapper.map(post,PostDto.class)).collect(Collectors.toList());
    }

    //convert DTO to entity
    private Post dtoToEntity(PostDto postDto)
    {
      /*  Post post=new Post();
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        post.setTitle(postDto.getTitle());
        return post;*/
        Post post=mapper.map(postDto,Post.class);
        return post;
    }

    //convert entity to DTO
    private PostDto entityToDto(Post post)
    {
    /*PostDto postDto=new PostDto();
    postDto.setTitle(post.getTitle());
    postDto.setDescription(post.getDescription());
    postDto.setId(post.getId());
    postDto.setContent(post.getContent());
    return postDto;*/
        PostDto postDto=mapper.map(post,PostDto.class);
        return postDto;
    }
}
