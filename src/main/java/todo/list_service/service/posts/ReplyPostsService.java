package todo.list_service.service.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.list_service.domain.posts.Posts;
import todo.list_service.domain.replyposts.ReplyPosts;
import todo.list_service.domain.replyposts.ReplyPostsRepository;
import todo.list_service.domain.user.User;
import todo.list_service.web.dto.posts.ReplyPostsResponseDto;
import todo.list_service.web.dto.posts.ReplyPostsSaveRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReplyPostsService {
    private final ReplyPostsRepository replyPostsRepository;

    @Transactional
    public Long save(ReplyPostsSaveRequestDto requestDto, User user, Posts posts){
        ReplyPosts savedReplyPosts;

        if (requestDto.getReplyLevel()==0){
            // 첫 댓글: level =0, order= 0 group = 자기댓글id
            ReplyPosts replyPosts = ReplyPosts.builder()
                    .user(user)
                    .replyContent(requestDto.getContent())
                    .replyLevel(requestDto.getReplyLevel())
                    .replyParent(requestDto.getReplyParent())
                    .build();
            replyPosts.setPosts(posts);

            savedReplyPosts = replyPostsRepository.save(replyPosts);
            savedReplyPosts.setReplyGroup(savedReplyPosts.getId());

        }else {
            // 대 댓글
            ReplyPosts replyPosts = ReplyPosts.builder()
                    .user(user)
                    .replyContent(requestDto.getContent())
                    .replyLevel(requestDto.getReplyLevel())
                    .replyParent(requestDto.getReplyParent())
                    .replyGroup(requestDto.getReplyGroup()).build();
            replyPosts.setPosts(posts);
            savedReplyPosts = replyPostsRepository.save(replyPosts);
        }



        return savedReplyPosts.getId();
    }

    @Transactional
    public Long update(ReplyPostsSaveRequestDto requestDto,Long replyId ,User user){
        ReplyPosts replyPosts = replyPostsRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id: " + replyId));

        replyPosts.update(requestDto);

        return replyPosts.getId();
    }

    @Transactional
    public Long delete(Long replyId, Posts posts) {
        ReplyPosts replyPosts = replyPostsRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id: " + replyId));
        if (replyPosts.getReplyGroup().equals(replyId)){
           // 댓글 저장당시 replyGroup을 제일 머리 댓글 id로 설정. 때문에 같으면 아래댓글 다 찾아서 삭제
            List<ReplyPosts> replyPostsList = replyPostsRepository.findAllByReplyGroup(replyId);
            for (ReplyPosts reply : replyPostsList){
                reply.setPosts(null);
                replyPostsRepository.delete(reply);
            }
        }else {
            replyPosts.setPosts(null);
            replyPostsRepository.delete(replyPosts);
        }


        return replyId;
    }

    @Transactional
    public void deleteAll(Long postId) {
        List<ReplyPosts> replyPostsList = replyPostsRepository.findAllByPostId(postId);
        if (replyPostsList != null && !replyPostsList.isEmpty()) {
            for (ReplyPosts replyPosts : replyPostsList) {
                replyPosts.setPosts(null);
                replyPostsRepository.delete(replyPosts);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ReplyPostsResponseDto> findByPostIdToShow(Long id) {
        List<ReplyPosts> replyPostsList = replyPostsRepository.findAllByPostIdAndSort(id);
        List<ReplyPostsResponseDto> responseDtos = replyPostsList.stream()
                .map(p -> new ReplyPostsResponseDto(p)).collect(Collectors.toList());

        return responseDtos;
    }
}
