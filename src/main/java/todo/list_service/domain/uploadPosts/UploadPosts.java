package todo.list_service.domain.uploadPosts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list_service.domain.BaseTimeEntity;
import todo.list_service.domain.posts.Posts;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class UploadPosts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UPLOAD_ID")
    private Long id;

    @ManyToOne(targetEntity = Posts.class)
    @JoinColumn(name = "POSTS_ID")
    private Posts posts;

    @Column(nullable = false)
    private String originalFileName;
    @Column(nullable = false)
    private String storedFileName;
    private Long fileSize;

    @Builder
    public UploadPosts(String originalFileName, String storedFileName, Long fileSize) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fileSize = fileSize;
    }

    public void setPosts(Posts posts) {
        if (this.posts != null) {
            this.posts.getUploadPosts().remove(this);
        }
        if (posts != null) {
            this.posts = posts;
            posts.getUploadPosts().add(this);
        }else{
            this.posts = null;
        }

    }

}
