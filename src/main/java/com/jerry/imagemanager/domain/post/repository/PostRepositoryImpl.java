package com.jerry.imagemanager.domain.post.repository;

import com.jerry.imagemanager.domain.post.Post;
import com.jerry.imagemanager.global.common.util.RepositorySliceHelper;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static com.jerry.imagemanager.domain.post.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Post> findPosts(Pageable pageable) {
        List<Post> posts = queryFactory
                .selectFrom(post)
                .orderBy(new OrderSpecifier<>(Order.DESC, post.cratedAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return RepositorySliceHelper.toSlice(posts, pageable);
    }
}
