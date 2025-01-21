package com.PenHub.PenHub.enteties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "posts")
@NamedEntityGraph(name = "post.tags",attributeNodes = @NamedAttributeNode("tags"))
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private String title;
    private String Description;

//    private LinkedHashSet<String> tags;

        @ManyToMany(cascade = {CascadeType.ALL})
        @JoinTable(
                name="posts_tags",
                joinColumns = {@JoinColumn(name="post_id")},
                inverseJoinColumns = {@JoinColumn(name="tag_id")}
        )

        private Set<Tag> tags=new HashSet<>();

}
