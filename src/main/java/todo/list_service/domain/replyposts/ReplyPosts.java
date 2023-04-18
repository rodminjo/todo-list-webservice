package todo.list_service.domain.replyposts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import todo.list_service.domain.BaseTimeEntity;
import todo.list_service.domain.posts.Posts;
import todo.list_service.domain.user.User;
import todo.list_service.web.dto.posts.ReplyPostsSaveRequestDto;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ReplyPosts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLYPOSTS_ID")
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(targetEntity = Posts.class)
    @JoinColumn(name = "POSTS_ID")
    private Posts posts;

    @Column(columnDefinition = "TEXT", length = 500)
    private String replyContent;

    @Column
    private Integer replyLevel;

    @Column
    private String replyParent;

    @Column
    private Long replyGroup;

    @Builder
    public ReplyPosts(User user, String replyContent, Integer replyLevel, String replyParent, Long replyGroup) {
        this.user = user;
        this.replyContent = replyContent;
        this.replyLevel = replyLevel;
        this.replyParent = replyParent;
        this.replyGroup = replyGroup;
    }

    public void setPosts(Posts posts){
            if (this.posts != null) {
                this.posts.getReplyPosts().remove(this);
            }

            if (posts != null) {
                this.posts = posts;
                posts.getReplyPosts().add(this);
            }else{
                this.posts = null;
            }
    }

    public void addUser(User user){
        this.user = user;
    }

    public void setReplyGroup(Long replyGroup) {
        this.replyGroup = replyGroup;
    }

    public void update(String replyContent){
        this.replyContent = replyContent;
    }
}
