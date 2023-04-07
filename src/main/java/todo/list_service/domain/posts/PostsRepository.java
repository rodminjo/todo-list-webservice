package todo.list_service.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

    @Query("SELECT p FROM Posts p ORDER BY p.replyPosts.size DESC")
    List<Posts> findAllCommentDesc();

    @Query("SELECT p FROM Posts p ORDER BY p.viewCount DESC")
    List<Posts> findAllLookDesc();

    @Query("SELECT p FROM Posts p WHERE p.title LIKE %:search% OR p.user.nickName LIKE %:search% ORDER BY p.id DESC")
    List<Posts> findBySearchDesc(@Param("search") String search);

    @Query("SELECT p FROM Posts p WHERE p.title LIKE %:search% OR p.user.nickName LIKE %:search% ORDER BY p.replyPosts.size DESC")
    List<Posts> findBySearchAndCommentDesc(@Param("search") String search);

    @Query("SELECT p FROM Posts p WHERE p.title LIKE %:search% OR p.user.nickName LIKE %:search% ORDER BY p.viewCount DESC")
    List<Posts> findBySearchAndLookDesc(@Param("search") String search);

}
