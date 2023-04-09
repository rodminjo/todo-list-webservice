package todo.list_service.web.dto.todo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.todo.Todo;

import java.io.File;


@Getter
@NoArgsConstructor
public class TodosListResponseDto {

    private Long id;
    private String title;
    private Boolean checked;
    private String todoDate;
    private Integer importance;
    private String storedFileName;

    public TodosListResponseDto(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.checked = todo.getChecked();
        this.importance = todo.getImportance();
        this.todoDate = getTodoDate();

    }

    public void addStoredFileName(String storedFileName){

        this.storedFileName = storedFileName;
    }
}
