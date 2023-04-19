package todo.list_service.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import todo.list_service.service.posts.PostsService;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PostsIndexController.class)
@AutoConfigureWebMvc    // 이 어노테이션으로 MockMvc를 Builder없이 주입받을 수 있음
class PostsIndexControllerTest {

    // TimerIndexController에서 잡고있는 bean객체들을 MockBean으로 주입
    @MockBean
    private PostsService postsService;

    @Autowired
    private MockMvc mockMvc;

    // 스프링 시큐리티가 실행된 상태에서 테스트를 하기 위한 설정
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    @WithMockUser
    void 게시글목록_불러오기() throws Exception {

        // given 객체가 특정상황에서 해야되는 행위를 정의한 메서드. 즉 findAll(1)은 postsResponse를 보내준다
        given(postsService.findAll(1)).willReturn(new ArrayList<>());

        // 신호를 보내주고 기대값이 나왔는지 확인할 수 있는 메서드
        mockMvc.perform(get("/posts?sort=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts-index"));

        // 해당 메서드가 작동했는지 알려주는 메서드
        verify(postsService).findAll(1);
    }


    @Test
    @WithMockUser
    void 게시글목록_검색결과_불러오기() throws Exception {

        // given 객체가 특정상황에서 해야되는 행위를 정의한 메서드.
        given(postsService.findBySearch("aa",1)).willReturn(new ArrayList<>());

        // 신호를 보내주고 기대값이 나왔는지 확인할 수 있는 메서드
        mockMvc.perform(get("/posts?sort=1&search=aa"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts-index"));

        verify(postsService).findBySearch("aa",1);
    }



}