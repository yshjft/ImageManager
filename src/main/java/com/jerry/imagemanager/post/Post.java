package com.jerry.imagemanager.post;

import com.jerry.imagemanager.common.entity.BaseEntity;
import com.jerry.imagemanager.image.PostImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @OneToMany(mappedBy = "post")
    List<PostImage> postImages = new ArrayList<>();

    public Post(String title) {
        // validation 필요
        this.title = title;
    }
}
