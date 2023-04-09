package todo.list_service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import todo.list_service.config.auth.CustomOAuth2UserService;
import todo.list_service.config.auth.dto.SessionUser;
import todo.list_service.domain.timer.TimerRepository;
import todo.list_service.domain.user.Role;
import todo.list_service.domain.user.User;
import todo.list_service.domain.user.UserRepository;
import todo.list_service.service.timer.TimerService;
import todo.list_service.web.dto.timer.TimerResponseDto;
import todo.list_service.web.dto.timer.TimerSaveRequestDto;
import todo.list_service.web.dto.timer.TimerUpdateRequestDto;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimerApiControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimerService timerService;

    @Autowired
    private TimerRepository timerRepository;


    private MockMvc mvc;

    @LocalServerPort
    private int port;

    @Autowired
    WebApplicationContext context;

    @BeforeEach
    void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // User setting
        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        timerRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void timer_저장하기() throws Exception {
        User user = userRepository.findAll().get(0);
        MockHttpSession session = new MockHttpSession();

        session.setAttribute("user",new SessionUser(user));
        // Timer
        String title = "test title";
        String content = "test content";
        //Category category = Category.STUDY;
        String category = "STUDY";
        LocalDateTime startTime = LocalDateTime.of(2022,01,01,0,0,0);
        Integer doingTime = 3661;



        TimerSaveRequestDto requestDto = TimerSaveRequestDto.builder().title(title).content(content)
                .category(category).startTime(startTime).doingTime(doingTime).build();


        String url = "http://localhost:" + port + "/api/v1/timer";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestDto)))
                .andExpect(status().isOk());


    }

    @Test
    @WithMockUser
    void timer_수정하기() throws Exception {
        User user = userRepository.findAll().get(0);
        MockHttpSession session = new MockHttpSession();

        session.setAttribute("user", new SessionUser(user));

        // Timer 객체 생성후 저장
        String title = "test title";
        String content = "test content";
        //Category category = Category.STUDY;
        String category = "STUDY";
        LocalDateTime startTime = LocalDateTime.of(2022, 01, 01, 0, 0, 0);
        Integer doingTime = 3661;


        TimerSaveRequestDto requestDto = TimerSaveRequestDto.builder().title(title).content(content)
                .category(category).startTime(startTime).doingTime(doingTime).build();
        requestDto.setUserId(user.getId());

        Long savedId = timerService.save(requestDto);

        //수정하기
        String updateTitle = "updateTitle";
        String updateContent = "update Content";
        String updateCategory = "WORK";

        TimerUpdateRequestDto updateRequestDto = TimerUpdateRequestDto.builder()
                .title(updateTitle).content(updateContent).category(updateCategory).build();
        updateRequestDto.setId(savedId);

        String url = "http://localhost:" + port + "/api/v1/timer/" + savedId;

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk());

        //then
        TimerResponseDto responseDto = timerService.findById(savedId);
        Assertions.assertThat(responseDto.getTitle()).isEqualTo(updateTitle);
        Assertions.assertThat(responseDto.getContent()).isEqualTo(updateContent);
        Assertions.assertThat(responseDto.getCategory()).isEqualTo(updateCategory);
    }
}