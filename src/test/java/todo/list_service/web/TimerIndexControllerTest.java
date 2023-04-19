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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import todo.list_service.config.oauth.dto.SessionUser;
import todo.list_service.domain.user.Role;
import todo.list_service.domain.user.User;

import todo.list_service.service.timer.TimerService;
import todo.list_service.web.dto.timer.TimerResponseDto;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(TimerIndexController.class)
@AutoConfigureWebMvc    // 이 어노테이션으로 MockMvc를 Builder없이 주입받을 수 있음
class TimerIndexControllerTest {

    @MockBean
    private TimerService timerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }


    @Test
    @WithMockUser
    void 타이머_메인화면_목록_불러오기() throws Exception {

        //Mock User 생성
        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user",new SessionUser(user));


        String date = "2023-04-01";

        // given 객체가 특정상황에서 해야되는 행위를 정의한 메서드. 즉 findByUserIdAndTakeAllDesc("2023-04-01",1L)는 ArrayList를 보내준다
        given(timerService.findByUserIdAndTakeAllDesc(date,null))
                .willReturn(new ArrayList<>());

        // 신호를 보내주고 기대값이 나왔는지 확인할 수 있는 메서드
        mockMvc.perform(get("/timer/2023-04-01").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("timer-index"));

        // 해당 메서드가 작동했는지 알려주는 메서드
        verify(timerService).findByUserIdAndTakeAllDesc(date,null);
    }

    @Test
    @WithMockUser
    void 타이머_저장화면_불러오기() throws Exception {

        // 신호를 보내주고 기대값이 나왔는지 확인할 수 있는 메서드
        mockMvc.perform(get("/timer/save"))
                .andExpect(status().isOk())
                .andExpect(view().name("timer-save"));

    }

    @Test
    @WithMockUser
    void 타이머_상세화면_불러오기() throws Exception {

        // given 객체가 특정상황에서 해야되는 행위를 정의한 메서드. 즉 findById(1L)는 TimerResponseDto 보내준다
        given(timerService.findById(1L))
                .willReturn(new TimerResponseDto());

        // 신호를 보내주고 기대값이 나왔는지 확인할 수 있는 메서드
        mockMvc.perform(get("/timer/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("timer-update"));

        // 해당 메서드가 작동했는지 알려주는 메서드
        verify(timerService).findById(1L);
    }
}