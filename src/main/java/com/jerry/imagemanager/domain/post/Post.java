package com.jerry.imagemanager.domain.post;

import com.jerry.imagemanager.global.common.entity.BaseEntity;
import com.jerry.imagemanager.global.error.exception.InvalidRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.jerry.imagemanager.global.error.ErrorCode.BAD_REQUEST;
import static org.springframework.util.StringUtils.hasText;

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
        setTitle(title);
    }

    private void setTitle(String title) {
        if(!hasText(title)) {
            throw new InvalidRequestException(BAD_REQUEST);
        }

        if(title.length() > 100) {
            throw new InvalidRequestException(BAD_REQUEST);
        }

        this.title = title;
    }
}
