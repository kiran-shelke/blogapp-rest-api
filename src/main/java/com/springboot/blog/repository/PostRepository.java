package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findByCategoryId(Long categoryId);
    @Query("SELECT p FROM Post p WHERE p.title LIKE CONCAT('%',:query,'%') or p.description LIKE CONCAT('%',:query,'%')")
    List<Post> searchPost(String query);

    @Query(value = "Select * from posts p where p.title like concat('%',:query,'%') or p.description like concat('%',:query,'%'", nativeQuery = true)
    List<Post> searchPostSQL(String query);
}
