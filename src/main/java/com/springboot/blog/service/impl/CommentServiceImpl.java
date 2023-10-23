package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper=mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        //convert DTO(CommentDto) to Entity(Comment)
        Comment comment=dtoToEntity(commentDto);
        //retrieve post entity by id
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));

        //set post to comment entity
        comment.setPost(post);
        //save comment entity to DB
        Comment savedComment= commentRepository.save(comment);
        //convert Entity(Comment) to DTO(CommentDTO) and return it
        return entityToDto(savedComment);
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        //retrieve post by postId
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        //retrieve comments by post id
        List<Comment> comments=commentRepository.findByPostId(postId);
        //convert list of comment entity to list of comment dto's and return it
        return comments.stream().map(comment->entityToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        //retrieve post by postId
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        //retrieve comment by commentId
        Comment comment=commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));

        //business logic to check whether comment is belong to post or not
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        return entityToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto requestDto) {
        //retrieve post by postId
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        //retrieve comment by commentId
        Comment comment=commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));

        //business logic to check whether comment is belong to post or not
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        //set value to comment entity
        comment.setBody(requestDto.getBody());
        comment.setEmail(requestDto.getEmail());
        comment.setName(requestDto.getName());
        //save comment object
        Comment updatedComment= commentRepository.save(comment);
        //convert comment entity to comment dto and return it
        return entityToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        //retrieve post by postId
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        //retrieve comment by commentId
        Comment comment=commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));
        //business logic to check whether comment is belong to post or not
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        commentRepository.delete(comment);
    }

    private CommentDto entityToDto(Comment comment)
    {
        /*CommentDto commentDto=new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setBody(comment.getBody());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        return commentDto;*/
        return mapper.map(comment,CommentDto.class);
    }

    private Comment dtoToEntity(CommentDto commentDto)
    {
        /*Comment comment=new Comment();
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        comment.setName(commentDto.getName());
        comment.setId(commentDto.getId());
        return comment;*/
        return mapper.map(commentDto,Comment.class);
    }
}
