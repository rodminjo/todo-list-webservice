package todo.list_service.domain.todo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.BaseTimeEntity;
import todo.list_service.domain.uploadTodo.UploadTodo;
import todo.list_service.domain.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Todo extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TODO_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL)
    private List<UploadTodo> uploadTodo = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false, length = 500)
    private String content;

    @Column(columnDefinition = "boolean default false")
    private Boolean checked;

    @Column(length = 10, nullable = false)
    private String todoDate;

    @Column(columnDefinition = "integer default 1")
    private Integer importance;


    @Builder
    public Todo(User user, String title, String content, String todoDate) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.todoDate = todoDate;

    }

    public void update(String title, String content, String todoDate){
        this.title = title;
        this.content = content;
        this.todoDate = todoDate;
    }

    public void importanceChange(Integer importance){
        this.importance = importance;
    }

    public void checkedChange(Boolean checked){
        this.checked = checked;
    }

}
