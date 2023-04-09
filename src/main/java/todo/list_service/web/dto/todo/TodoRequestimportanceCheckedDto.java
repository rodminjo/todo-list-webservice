package todo.list_service.web.dto.todo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoRequestimportanceCheckedDto {

    private Long id;
    private Integer importance;
    private Boolean checked;


}
