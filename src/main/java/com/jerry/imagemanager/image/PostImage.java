package com.jerry.imagemanager.image;

import com.jerry.imagemanager.common.entity.BaseEntity;
import com.jerry.imagemanager.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "PostImages")
@NoArgsConstructor
@Getter
public class PostImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @Column(name = "image_url", nullable = false, length=2083)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    public PostImage(String imageUrl, Post post) {
        // validation
        this.imageUrl = imageUrl;
        setPost(post);
    }

    private void setPost(Post post) {
        this.post = post;
        post.getPostImages().add(this);
    }
}
