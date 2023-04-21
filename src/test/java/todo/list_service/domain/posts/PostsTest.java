package todo.list_service.domain.posts;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class PostsTest {

    @Test
    void 제목과_내용을_수정한다() {
        Posts posts = Posts.builder().title("test title").content("test contetnt").viewCount(0).build();

        posts.update("change title","change content");

        Assertions.assertThat(posts.getTitle()).isEqualTo("change title");
        Assertions.assertThat(posts.getContent()).isEqualTo("change content");

    }

    @Test
    void 조회수를_올린다() {

        Posts posts = Posts.builder().title("test title").content("test contetnt").viewCount(0).build();

        posts.updateViewCount();

        Assertions.assertThat(posts.getViewCount()).isEqualTo(1);
    }
}