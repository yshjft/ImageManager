package com.jerry.imagemanager.global.common.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
public class PageResponse<T> {
  private List<T> content;
  private int numberOfElements;
  private int pageNumber;
  private int pageSize;
  private boolean hasNext;


  @Builder
  public PageResponse(Slice<T> slice) {
    this.content = slice.getContent();
    this.numberOfElements = slice.getNumberOfElements();
    this.pageNumber = slice.getPageable().getPageNumber();
    this.pageSize = slice.getPageable().getPageSize();
    this.hasNext = slice.hasNext();
  }
}
