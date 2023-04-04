package todo.list_service.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import todo.list_service.domain.BaseTimeEntity;
import todo.list_service.domain.replyposts.ReplyPosts;
import todo.list_service.domain.uploadPosts.UploadPosts;
import todo.list_service.domain.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@DynamicInsert
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSTS_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column
    @ColumnDefault("0")
    private Integer viewCount;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL)
    private List<UploadPosts> uploadPosts= new ArrayList<>();

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL)
    private List<ReplyPosts> replyPosts= new ArrayList<>();


    @Builder
    public Posts(User user, String title, String content, Integer viewCount) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.viewCount = viewCount;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateViewCount(){
        this.viewCount++;
    }

}
