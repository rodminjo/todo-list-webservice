package todo.list_service.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import todo.list_service.config.oauth.dto.SessionUser;
import todo.list_service.domain.timer.TimerRepository;
import todo.list_service.domain.user.Role;
import todo.list_service.domain.user.User;
import todo.list_service.domain.user.UserRepository;
import todo.list_service.service.timer.TimerService;
import todo.list_service.web.dto.timer.TimerListResponseDto;
import todo.list_service.web.dto.timer.TimerResponseDto;
import todo.list_service.web.dto.timer.TimerSaveRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimerIndexControllerTest {
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
    void timer_목록불러오기(){
        User user = userRepository.findAll().get(0);
        MockHttpSession session = new MockHttpSession();

        session.setAttribute("user",new SessionUser(user));

        // Timer 객체 생성후 저장
        String title = "test title";
        String content = "test content";
        //Category category = Category.STUDY;
        String category = "STUDY";
        LocalDateTime startTime = LocalDateTime.of(2022,01,01,0,0,0);
        Integer doingTime = 3661;



        TimerSaveRequestDto requestDto = TimerSaveRequestDto.builder().title(title).content(content)
                .category(category).startTime(startTime).doingTime(doingTime).build();
        requestDto.setUserId(user.getId());

        Long savedId = timerService.save(requestDto);

        // 목록 불러오기
        String date = LocalDate.now().toString();
        List<TimerListResponseDto> byUserIdAndTakeAllDesc = timerService.findByUserIdAndTakeAllDesc(date, user.getId());
        TimerListResponseDto timerListResponseDto = byUserIdAndTakeAllDesc.get(0);

        Assertions.assertThat(timerListResponseDto.getId()).isEqualTo(savedId);
        Assertions.assertThat(timerListResponseDto.getTitle()).isEqualTo(title);
        Assertions.assertThat(timerListResponseDto.getCategory()).isEqualTo(category);
        Assertions.assertThat(timerListResponseDto.getDoingTime()).isEqualTo(doingTime);

    }

    @Test
    @WithMockUser
    void timer_상세불러오기() {
        User user = userRepository.findAll().get(0);
        MockHttpSession session = new MockHttpSession();

        session.setAttribute("user", new SessionUser(user));

        // Timer 객체 생성후 저장
        String title = "test title";
        String content = "test content";
        String category = "STUDY";
        LocalDateTime startTime = LocalDateTime.of(2022, 01, 01, 0, 0, 0);
        Integer doingTime = 3661;


        TimerSaveRequestDto requestDto = TimerSaveRequestDto.builder().title(title).content(content)
                .category(category).startTime(startTime).doingTime(doingTime).build();
        requestDto.setUserId(user.getId());

        Long savedId = timerService.save(requestDto);

        //불러오기
        TimerResponseDto responseDto = timerService.findById(savedId);


        Assertions.assertThat(responseDto.getTitle()).isEqualTo(title);
        Assertions.assertThat(responseDto.getContent()).isEqualTo(content);
        Assertions.assertThat(responseDto.getCategory()).isEqualTo(category);
        Assertions.assertThat(responseDto.getStartTime()).isEqualTo(startTime);
        Assertions.assertThat(responseDto.getDoingTime()).isEqualTo(doingTime);

    }
}