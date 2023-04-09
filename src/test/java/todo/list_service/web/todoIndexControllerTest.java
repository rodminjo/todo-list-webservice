package todo.list_service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import todo.list_service.config.auth.dto.SessionUser;
import todo.list_service.domain.user.Role;
import todo.list_service.domain.user.User;
import todo.list_service.domain.user.UserRepository;
import todo.list_service.service.todo.TodoService;
import todo.list_service.web.dto.todo.TodoResponseDto;
import todo.list_service.web.dto.todo.TodoSaveRequestDto;
import todo.list_service.web.dto.todo.TodosListResponseDto;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class todoIndexControllerTest {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserRepository userRepository;

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

    }


    @Test
    @WithMockUser("USER")
    void todo_초기화면_날짜입력안할때_today불러오기() throws Exception {
        //given
        RequestBuilder rb = MockMvcRequestBuilders.get("/todo");
        //when

        //then
        mvc.perform(rb).andExpect(MockMvcResultMatchers.redirectedUrl("/todo/"+LocalDate.now().toString()))
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
        mvc.perform(rb).andExpect(status().isOk());

    }
    @Test
    @WithMockUser(roles = "USER")
    void todoUpdate_앞에서_저장한_내용_수정폼에서_불러오기() throws Exception {
        //given
        // 테스트 유저 저장
        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();
        userRepository.save(user);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new SessionUser(user));

        //저장할 데이터 생성
        String title = "test title";
        String content = "test content";
        String todoDate = "1111-01-01";
        TodoSaveRequestDto build = TodoSaveRequestDto.builder().title(title).content(content).todoDate(todoDate).build();
        build.setUserId(user.getId());

        // 데이터 저장
        Long savedTodoId = todoService.save(build, null);

        // 저장한 데이터 찾아오기
        TodoResponseDto dto =todoService.findByIdToShow(savedTodoId);
        //when
        RequestBuilder rb = MockMvcRequestBuilders.get("/todo/save/" + savedTodoId).session(session);

        //then
        // 잘 응답하는지 확인
        mvc.perform(rb).andExpect(status().isOk());

        //불러오는게 맞는값인지 확인
        Assertions.assertThat(dto.getTitle()).isEqualTo(title);
        Assertions.assertThat(dto.getContent()).isEqualTo(content);
        Assertions.assertThat(dto.getTodoDate()).isEqualTo(todoDate);
    }

    @Test
    @WithMockUser
    void todo_index_출력확인() throws Exception {
        // User setting
        String title1 = "test title";
        String content1 = "test content";
        String todoDate1 = "2022-01-01";

        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();
        userRepository.save(user);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new SessionUser(user));

        // 자료 1 (메서드 저장)
        TodoSaveRequestDto requestDto = TodoSaveRequestDto.builder()
                .title(title1).content(content1).todoDate(todoDate1).build();
        requestDto.setUserId(user.getId());

        todoService.save(requestDto, null);

        // 자료 2 (보내서 받기)
        String title2 = "test title2";
        String content2 = "test content2";
        String todoDate2 = "2022-01-01";


        TodoSaveRequestDto requestDto2 = TodoSaveRequestDto.builder()
                .title(title2).content(content2).todoDate(todoDate2).build();
        requestDto2.setUserId(user.getId());

        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
                new FileInputStream("/Users/jogyeongmin/Downloads/미모티콘.png"));
        MockMultipartFile mock = new MockMultipartFile("key", "key", "application/json", new ObjectMapper().writeValueAsString(requestDto2).getBytes());

        String url = "http://localhost:"+port+"/api/v1/todo";

        mvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(file)
                        .file(mock)
                        .session(session))
                .andExpect(status().isOk());

        List<TodosListResponseDto> byUserIdAndTakeAllDesc = todoService.findByUserIdAndTakeAllDesc(todoDate1, user.getId());

        Assertions.assertThat(byUserIdAndTakeAllDesc.size()).isEqualTo(2);
        Assertions.assertThat(byUserIdAndTakeAllDesc.get(0).getTitle()).isEqualTo(title1);
        Assertions.assertThat(byUserIdAndTakeAllDesc.get(1).getTitle()).isEqualTo(title2);
        Assertions.assertThat(byUserIdAndTakeAllDesc.get(1).getStoredFileName()).contains(".png");

    }

    @Test
    @WithMockUser
    void update창_정보불러오기() throws Exception {
        // User setting
        String title1 = "test title";
        String content1 = "test content";
        String todoDate1 = "2022-01-01";

        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();
        userRepository.save(user);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new SessionUser(user));

        // 자료 1 (메서드 저장)
        TodoSaveRequestDto requestDto = TodoSaveRequestDto.builder()
                .title(title1).content(content1).todoDate(todoDate1).build();
        requestDto.setUserId(user.getId());

        Long savedTodoId = todoService.save(requestDto, null);

        TodoResponseDto byIdToShow = todoService.findByIdToShow(savedTodoId);

        Assertions.assertThat(byIdToShow.getId()).isEqualTo(savedTodoId);
        Assertions.assertThat(byIdToShow.getTitle()).isEqualTo(title1);
        Assertions.assertThat(byIdToShow.getTodoDate()).isEqualTo(todoDate1);
        Assertions.assertThat(byIdToShow.getContent()).isEqualTo(content1);
    }



}