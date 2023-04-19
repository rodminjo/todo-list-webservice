package todo.list_service.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import todo.list_service.config.oauth.dto.SessionUser;
import todo.list_service.domain.user.Role;
import todo.list_service.domain.user.User;

import todo.list_service.service.todo.TodoService;

import todo.list_service.web.dto.todo.TodoResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(TodoIndexController.class)
@AutoConfigureWebMvc    // 이 어노테이션으로 MockMvc를 Builder없이 주입받을 수 있음
class todoIndexControllerTest {

    @MockBean
    private TodoService todoService;

    @Autowired
    private MockMvc mockMvc;

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
    @WithMockUser("USER")
    void todo_초기화면_날짜입력안할때_today불러오기() throws Exception {
        //given
        RequestBuilder rb = MockMvcRequestBuilders.get("/todo");
        //when

        //then
        mockMvc.perform(rb).andExpect(MockMvcResultMatchers.redirectedUrl("/todo/"+LocalDate.now().toString()))
                .andExpect(status().is3xxRedirection());

    }

    @Test
    @WithMockUser("USER")
    void todoSave_앞에서_지정한_날짜_저장폼에서_불러오기() throws Exception {
        //given
        String todoDate = "2022-01-01";
        RequestBuilder rb = MockMvcRequestBuilders.get("/todo/save/"+todoDate);
        //when

        //then
        mockMvc.perform(rb).andExpect(status().isOk());

    }
    @Test
    @WithMockUser(roles = "USER")
    void todoUpdate_앞에서_저장한_내용_수정폼에서_불러오기() throws Exception {

        // given 객체가 특정상황에서 해야되는 행위를 정의한 메서드. 즉 findById(1L)는 TimerResponseDto 보내준다
        given(todoService.findByIdToShow(1L))
                .willReturn(new TodoResponseDto());

        // 신호를 보내주고 기대값이 나왔는지 확인할 수 있는 메서드
        mockMvc.perform(get("/todo/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("todo-update"));

        // 해당 메서드가 작동했는지 알려주는 메서드
        verify(todoService).findByIdToShow(1L);
    }

    @Test
    @WithMockUser
    void todo_index_출력확인() throws Exception {

        //Mock User 생성
        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user",new SessionUser(user));

        String date = "2023-04-01";

        // given 객체가 특정상황에서 해야되는 행위를 정의한 메서드. 즉 findAll(1)은 postsResponse를 보내준다
        given(todoService.findByUserIdAndTakeAllDesc(date,null)).willReturn(new ArrayList<>());

        // 신호를 보내주고 기대값이 나왔는지 확인할 수 있는 메서드
        mockMvc.perform(get("/todo/"+date).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("todo-index"));

        // 해당 메서드가 작동했는지 알려주는 메서드
        verify(todoService).findByUserIdAndTakeAllDesc(date, null);

    }



}