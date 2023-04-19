package todo.list_service.domain.posts;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class PostsRepositoryTest {


    @Autowired
    PostsRepository postsRepository;


    @Test
    public void 게시글저장_불러오기(){
        //given
        String title = "test title";
        String content = "test content";


        postsRepository.save(Posts.builder().title(title).content(content).viewCount(0).build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        Assertions.assertThat(posts.getTitle()).isEqualTo(title);
        Assertions.assertThat(posts.getContent()).isEqualTo(content);

    }

    @Test
    void BaseTimeEntity_등록(){
        //given
        String title = "test title";
        String content = "test content";

        postsRepository.save(Posts.builder().title(title).content(content).viewCount(0).build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Assertions.assertThat(postsList.get(0).getCreatedDate()).isBefore(LocalDateTime.now());
        Assertions.assertThat(postsList.get(0).getModifiedDate()).isBefore(LocalDateTime.now());

        System.out.println("postsList.get(0).getCreatedDate() = " + postsList.get(0).getCreatedDate());
        System.out.println("postsList.get(0).getModifiedDate() = " + postsList.get(0).getModifiedDate());
    }




}