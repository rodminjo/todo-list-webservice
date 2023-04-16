package todo.list_service.web.dto.todo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.todo.Todo;

import java.util.List;


@Getter
@NoArgsConstructor
public class TodoSaveRequestDto {

    private Long userId;
    private String title;
    private String content;
    private String todoDate;

    @Builder
    public TodoSaveRequestDto(String title, String content, String todoDate) {
        this.title = title;
        this.content = content;
        this.todoDate = todoDate;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public Todo toEntity(){
        return Todo.builder().title(this.title).content(this.content).todoDate(this.todoDate).build();
    }

}
