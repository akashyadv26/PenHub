package com.PenHub.PenHub.enteties;

import com.PenHub.PenHub.Configurations.AuditingConfiguration;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "posts")
@NamedEntityGraph(name = "post.tags",attributeNodes = @NamedAttributeNode("tags"))
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private String title;
    private String description;

//    private LinkedHashSet<String> tags;

        @ManyToMany(cascade = {CascadeType.ALL})
        @JoinTable(
                name="posts_tags",
                joinColumns = {@JoinColumn(name="post_id")},
                inverseJoinColumns = {@JoinColumn(name="tag_id")}
        )

        private Set<Tag> tags=new HashSet<>();

        @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

        @LastModifiedDate
    private LocalDateTime lastModifiedDate;

        @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

        private String imageUrl;
}
