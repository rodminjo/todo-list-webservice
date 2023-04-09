package todo.list_service.web.dto.todo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.todo.Todo;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class TodoResponseDto {

    private Long id;
    private String title;
    private String content;
    private String todoDate;
    private List<String> todoImg;

    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.content = todo.getContent();
        this.todoDate = todo.getTodoDate();
        this.todoImg = todo.getUploadTodo().stream()
                .map(uploadTodo -> uploadTodo.getStoredFileName()).collect(Collectors.toList());
    }
}
