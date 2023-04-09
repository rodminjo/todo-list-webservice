package todo.list_service.domain.todo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @AfterEach
    void afterEach(){
        todoRepository.deleteAll();
    }


    @Test
    void 등록하다(){
        //given
        String title = "test title";
        String content = "test content";
        String todoDate = "2022-01-01";

        Todo savedTodo = todoRepository.save(Todo.builder()
                .title(title)
                .content(content)
                .todoDate(todoDate).build());

        List<Todo> all = todoRepository.findAll();

        Todo searchedTodo = all.get(0);
        Assertions.assertThat(searchedTodo.getTitle()).isEqualTo(savedTodo.getTitle());
        Assertions.assertThat(searchedTodo.getTodoDate()).isEqualTo(savedTodo.getTodoDate());
    }

    @Test
    void BaseTimeEntity_등록(){

        //given
        String title = "test title";
        String content = "test content";
        String todoDate = LocalDate.now().toString();

        Todo savedTodo = todoRepository.save(Todo.builder()
                        .title(title)
                        .content(content)
                        .todoDate(todoDate).build());

        //when
        List<Todo> all = todoRepository.findAll();

        //then
        Assertions.assertThat(all.get(0).getCreatedDate()).isBefore(LocalDateTime.now());
        Assertions.assertThat(all.get(0).getModifiedDate()).isBefore(LocalDateTime.now());

        System.out.println("all.get(0).getCreatedDate() = " + all.get(0).getCreatedDate());
        System.out.println("all.get(0).getModifiedDate() = " + all.get(0).getModifiedDate());
    }




}