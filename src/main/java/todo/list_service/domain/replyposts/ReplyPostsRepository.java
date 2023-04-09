package todo.list_service.domain.replyposts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplyPostsRepository extends JpaRepository<ReplyPosts, Long> {

    @Query("SELECT p FROM ReplyPosts p WHERE p.posts.id = :id")
    List<ReplyPosts> findAllByPostId(@Param("id") Long id);

    @Query("SELECT p FROM ReplyPosts p WHERE p.posts.id = :id ORDER BY p.replyGroup, p.createdDate")
    List<ReplyPosts> findAllByPostIdAndSort(@Param("id") Long id);

    @Query("SELECT p FROM ReplyPosts p WHERE p.replyGroup = :id")
    List<ReplyPosts> findAllByReplyGroup(@Param("id") Long id);
}
