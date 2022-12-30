package com.jerry.imagemanager.domain.post.repository;

import com.jerry.imagemanager.domain.post.Post;
import com.jerry.imagemanager.global.config.QuerydslTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QuerydslTestConfig.class})
class PostRepositoryImplTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("게시물 조회 테스트")
    void testFindPosts() {
        Post test1 = new Post("test1");
        Post test2 = new Post("test2");
        Post test3 = new Post("test3");
        Post test4 = new Post("test4");

        em.persist(test1);
        em.persist(test2);
        em.persist(test3);
        em.persist(test4);

        Slice<Post> slicedPosts = postRepository.findPosts(PageRequest.of(0, 3));

        assertThat(slicedPosts.getSize()).isEqualTo(3);
        assertThat(slicedPosts.getContent().get(0)).isEqualTo(test4);
    }

}