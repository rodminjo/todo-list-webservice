package todo.list_service.domain.uploadTodo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import todo.list_service.domain.BaseTimeEntity;
import todo.list_service.domain.todo.Todo;

import javax.persistence.*;


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


    /**
     * 설명 : JPA 연관관계 편의 메서드를 통해 uploadTodo를 set할 경우 todo에도 set되도록 설정
     * */
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
