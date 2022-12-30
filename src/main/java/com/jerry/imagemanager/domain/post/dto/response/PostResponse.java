package com.jerry.imagemanager.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponse {
    private Long id;
    private String title;
    private List<ImageResponse> images;

    @Builder
    public PostResponse(Long id, String title, List<ImageResponse> images) {
        this.id = id;
        this.title = title;
        this.images = images;
    }
}
