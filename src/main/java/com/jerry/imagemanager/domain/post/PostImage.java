package com.jerry.imagemanager.domain.post;

import com.jerry.imagemanager.global.common.entity.BaseEntity;
import com.jerry.imagemanager.global.error.exception.InvalidRequestException;
import com.jerry.imagemanager.global.common.util.UrlValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.jerry.imagemanager.global.error.ErrorCode.BAD_REQUEST;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@Entity
@Table(name = "PostImages")
@NoArgsConstructor
@Getter
public class PostImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_image_id")
    private Long id;

    @Column(name = "image_url", nullable = false, length=2083)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    public PostImage(String imageUrl, Post post) {
        setImageUrl(imageUrl);
        setPost(post);
    }

    private void setImageUrl(String imageUrl) throws InvalidRequestException {
        if(!hasText(imageUrl)) {
            throw new InvalidRequestException(BAD_REQUEST);
        }

        if(imageUrl.length() > 2083) {
            throw new InvalidRequestException(BAD_REQUEST);
        }

        if(!UrlValidator.isValidUrl(imageUrl)) {
            throw new InvalidRequestException(BAD_REQUEST);
        }

        this.imageUrl = imageUrl;
    }

    private void setPost(Post post) throws InvalidRequestException {
        if(isNull(post)) {
            throw new InvalidRequestException(BAD_REQUEST);
        }

        if(nonNull(this.post)) {
            post.getPostImages().remove(this);
        }

        this.post = post;
        post.getPostImages().add(this);
    }
}
