package todo.list_service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import todo.list_service.domain.posts.Posts;
import todo.list_service.domain.posts.PostsRepository;
import todo.list_service.web.dto.PostsSaveRequestDto;
import todo.list_service.web.dto.PostsUpdateRequestDto;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// TestRestTemplate 을 이용하여 실제로 Post신호를 줄건데 기존 포트가 아닌 다른 포트가 필요. 그래서 랜덤 포트로 실행
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PostsRepository postsRepository;
    
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    
    @BeforeEach
    void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    
    @AfterEach
    void tearDown() {
        postsRepository.deleteAll();
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void Posts_등록() throws Exception {
        //given
        String title = "test title";
        String content = "test content";
        Boolean checked = true;

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title).content(content).checked(checked).build();




        String url = "http://localhost:"+port+"/api/v1/posts/";

        //when
        // url을 통해 post를 보내줌
        ResponseEntity<Long> responseEntity = testRestTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(requestDto)))
                        .andExpect(status().isOk());

        //then
        // 바디에 들어있는지 확인
        List<Posts> all = postsRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(all.get(0).getContent()).isEqualTo(content);
        Assertions.assertThat(all.get(0).getChecked()).isSameAs(checked);

    }

    @Test
    @WithMockUser(roles = "USER")
    void Posts_수정() throws Exception {

        //given
        String title = "test title";
        String content = "test content";
        Boolean checked = true;

        PostsSaveRequestDto saveRequestDto = PostsSaveRequestDto.builder()
                .title(title).content(content).checked(checked).build();

        Long id = postsRepository.save(saveRequestDto.toEntity()).getId();


        String updatedTitle = "updated title";
        String updatedContent = "updated content";
        boolean updateChecked = false;

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(updatedTitle).content(updatedContent).checked(updateChecked).build();

        String url = "http://localhost:"+port+"/api/v1/posts/"+id+"/";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());


        //then
        List<Posts> all = postsRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(updatedTitle);
        Assertions.assertThat(all.get(0).getContent()).isEqualTo(updatedContent);
        Assertions.assertThat(all.get(0).getChecked()).isSameAs(updateChecked);

    }




}