package com.jerry.imagemanager.post;

import com.jerry.imagemanager.common.error.exception.InvalidRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.jerry.imagemanager.common.error.ErrorCode.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@DisplayName("Post 엔티티 테스트")
class PostTest {
    @Nested
    @DisplayName("Post 엔티티 생성 테스트")
    @TestInstance(PER_CLASS)
    class PostCreationTest {
        // fail
        @ParameterizedTest
        @MethodSource("titleParameter")
        @DisplayName("Post 엔티티 생성 실패 테스트")
        void testCreationFailCase(String title) {
            assertThatThrownBy(() -> new Post(title))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessage(BAD_REQUEST.getMessage());
        }

        // success
        @Test
        @DisplayName("Post 엔티티 생성 성공 테스트")
        void testCreationSuccessCase() {
            String title = "삼색이는 정말 귀여워!";
            Post post = new Post(title);

            assertThat(post).hasFieldOrPropertyWithValue("title", title);
        }


        private Stream<Arguments> titleParameter() {
            return Stream.of(null, Arguments.of(""), Arguments.of( "    "), Arguments.of("삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!삼색이 너무 귀여워!"));
        }
    }
}