package todo.list_service.domain.uploadTodo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import todo.list_service.domain.BaseTimeEntity;
import todo.list_service.domain.posts.Posts;
import todo.list_service.domain.todo.Todo;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class UploadTodo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UPLOAD_ID")
    private Long id;

    @ManyToOne(targetEntity = Todo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "TODO_ID")
    private Todo todo;

    @Column(nullable = false)
    private String originalFileName;
    @Column(nullable = false)
    private String storedFileName;
    private Long fileSize;


    public void setTodo(Todo todo) {
        if (this.todo != null) {
            this.todo.getUploadTodo().remove(this);
        }

        if (todo != null) {
            this.todo = todo;
            todo.getUploadTodo().add(this);
        }else{
            this.todo = null;
        }

    }

    @Builder
    public UploadTodo(String originalFileName, String storedFileName, Long fileSize) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fileSize = fileSize;
    }
}
