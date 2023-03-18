package todo.list_service.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.BaseTimeEntity;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "boolean default false")
    private Boolean checked;

    private String author;

    @Builder
    public Posts(String title, String content, Boolean checked, String author) {
        this.title = title;
        this.content = content;
        this.checked = checked;
        this.author = author;
    }

    public void update(String title, String content, Boolean checked) {
        this.title = title;
        this.content = content;
        this.checked = checked;

    }

}
