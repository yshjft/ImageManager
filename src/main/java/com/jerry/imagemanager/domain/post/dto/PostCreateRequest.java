package com.jerry.imagemanager.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostCreateRequest {
    @NotBlank
    private String title;

    @JsonCreator
    public PostCreateRequest(@JsonProperty("title") String title) {
        this.title = title;
    }
}
