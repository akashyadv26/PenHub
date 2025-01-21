package com.PenHub.PenHub.repositories;

import com.PenHub.PenHub.enteties.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {

    @EntityGraph(value = "post.tags")
    List<Post>findByTitle(String title);

    @EntityGraph(value = "post.tags")
    List<Post>findByTagsName(String tagname);



    @Override
    @EntityGraph(value = "post.tags")
    List<Post> findAll();


    @Query("SELECT p FROM posts  p WHERE p.title LIKE %:title%")
    List<Post>findByTitleContaining(@Param("title")String title);


    @Query(value = "SELECT * FROM posts  WHERE title LIKE %?1%",nativeQuery = true)
    List<Post>findByTitleNative(String title);
}
