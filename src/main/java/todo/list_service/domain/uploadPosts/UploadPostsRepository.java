package todo.list_service.domain.uploadPosts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UploadPostsRepository extends JpaRepository<UploadPosts,Long> {
    @Query("SELECT p FROM UploadPosts p WHERE p.posts.id = :id")
    List<UploadPosts> findAllByPostId(@Param("id") Long id);


}
