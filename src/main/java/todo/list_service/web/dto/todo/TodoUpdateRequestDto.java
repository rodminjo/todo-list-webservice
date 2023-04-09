package todo.list_service.web.dto.todo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@Getter
@NoArgsConstructor
public class TodoUpdateRequestDto {

    private Long todoId;
    private String title;
    private String content;
    private String todoDate;

    @Builder
    public TodoUpdateRequestDto(String title, String content, String todoDate) {
        this.title = title;
        this.content = content;
        this.todoDate = todoDate;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

}
