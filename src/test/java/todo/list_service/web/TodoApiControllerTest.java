package todo.list_service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import todo.list_service.config.oauth.dto.SessionUser;
import todo.list_service.domain.todo.Todo;
import todo.list_service.domain.todo.TodoRepository;
import todo.list_service.domain.uploadTodo.UploadTodoRepository;
import todo.list_service.domain.user.Role;
import todo.list_service.domain.user.User;
import todo.list_service.domain.user.UserRepository;
import todo.list_service.service.todo.TodoService;
import todo.list_service.web.dto.todo.TodoSaveRequestDto;
import todo.list_service.web.dto.todo.TodoUpdateRequestDto;
import todo.list_service.web.dto.todo.TodosListResponseDto;

import java.io.FileInputStream;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoApiControllerTest {

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UploadTodoRepository uploadTodoRepository;
    @Autowired
    private TodoService todoService;

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

    @AfterEach
    void tearDown() {
        uploadTodoRepository.deleteAll();
        todoRepository.deleteAll();
    }


    @Test
    @WithMockUser(roles = "USER")
    void todo_저장하기() throws Exception {
        //given
        // User setting
        String title = "test title to save";
        String content = "test content";
        String todoDate = "2022-01-01";

        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();
        userRepository.save(user);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user",new SessionUser(user));

        // PostSaveRequestDto 생성
        TodoSaveRequestDto requestDto = TodoSaveRequestDto.builder()
                .title(title).content(content).todoDate(todoDate).build();
        requestDto.setUserId(user.getId());

        // MockMultipartFile 생성
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
                new FileInputStream("/Users/jogyeongmin/Downloads/미모티콘.png"));
        MockMultipartFile mock = new MockMultipartFile("key", "key", "application/json", new ObjectMapper().writeValueAsString(requestDto).getBytes());
        // 보낼 url
        String url = "http://localhost:"+port+"/api/v1/todo";

        //when
        // url을 통해 post를 보내줌
        mvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(file)
                        .file(mock)
                        .session(session))
                .andExpect(status().isOk());


        //then
        // 바디에 들어있는지 확인
        List<Todo> all = todoRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser
    void todo_수정하기() throws Exception {
        //given
        // User setting
        String title = "test title to update";
        String content = "test content";
        String todoDate = "2022-01-01";

        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();
        userRepository.save(user);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user",new SessionUser(user));

        TodoSaveRequestDto requestDto = TodoSaveRequestDto.builder()
                .title(title).content(content).todoDate(todoDate).build();
        requestDto.setUserId(user.getId());




        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
                new FileInputStream("/Users/jogyeongmin/Downloads/미모티콘.png"));
        MockMultipartFile mock = new MockMultipartFile("key", "key", "application/json", new ObjectMapper().writeValueAsString(requestDto).getBytes());

        String url = "http://localhost:"+port+"/api/v1/todo";

        mvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(file)
                        .file(mock)
                        .session(session))
                .andExpect(status().isOk());

        List<Todo> findAll = todoRepository.findAll();
        Todo todo = findAll.get(findAll.size() - 1);

        String updatedTitle = " updated title";
        String updatedContent = " updated content";
        String updateTodoDate = "2022-01-02";

        TodoUpdateRequestDto updateRequestDto = TodoUpdateRequestDto.builder()
                .title(updatedTitle).content(updatedContent).todoDate(updateTodoDate).build();

        url = "http://localhost:"+port+"/api/v1/todo/"+todo.getId();
        mock = new MockMultipartFile("key", "key", "application/json", new ObjectMapper().writeValueAsString(updateRequestDto).getBytes());
        //when
        // url을 통해 post를 보내줌
        mvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(file)
                        .file(mock)
                        .session(session))
                .andExpect(status().isOk());


        //then
        // 정보가 수정됐는지 확인
        List<Todo> all = todoRepository.findAll();
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(updatedTitle);
        Assertions.assertThat(all.get(0).getContent()).isEqualTo(updatedContent);
        Assertions.assertThat(all.get(0).getTodoDate()).isEqualTo(updateTodoDate);

    }


    @Test
    @WithMockUser
    void todo_삭제하기() throws Exception {
        //given
        // User setting
        String title = "test title to delete";
        String content = "test content";
        String todoDate = "2022-01-01";

        User user = User.builder().name("testname").picture("test").email("testemail").role(Role.USER).build();
        userRepository.save(user);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user",new SessionUser(user));

        // 저장 데이터 준비
        TodoSaveRequestDto requestDto = TodoSaveRequestDto.builder()
                .title(title).content(content).todoDate(todoDate).build();
        requestDto.setUserId(user.getId());

        // 저장 post 요청
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png",
                new FileInputStream("/Users/jogyeongmin/Downloads/미모티콘.png"));
        MockMultipartFile mock = new MockMultipartFile("key", "key", "application/json", new ObjectMapper().writeValueAsString(requestDto).getBytes());

        String url = "http://localhost:"+port+"/api/v1/todo";

        // 저장 확인
        mvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(file)
                        .file(mock)
                        .session(session))
                .andExpect(status().isOk());

        List<Todo> findAll = todoRepository.findAll();
        Todo todo = findAll.get(findAll.size() - 1);

        // 삭제 url 준비
        url = "http://localhost:"+port+"/api/v1/todo/"+todo.getId();

        // 삭제 요청
        mvc.perform(MockMvcRequestBuilders.delete(url).session(session)).andExpect(status().isOk());

        // 리스트 받아오기
        List<TodosListResponseDto> byUserIdAndTakeAllDesc = todoService.findByUserIdAndTakeAllDesc(todoDate, user.getId());

        //비어있는지 확인
        Assertions.assertThat(byUserIdAndTakeAllDesc).isEmpty();
    }

}