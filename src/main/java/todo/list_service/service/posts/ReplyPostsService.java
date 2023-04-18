package todo.list_service.service.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import todo.list_service.domain.posts.Posts;
import todo.list_service.domain.replyposts.ReplyPosts;
import todo.list_service.domain.user.User;

import todo.list_service.domain.replyposts.ReplyPostsRepository;

import todo.list_service.web.dto.posts.ReplyPostsResponseDto;
import todo.list_service.web.dto.posts.ReplyPostsSaveRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReplyPostsService {
    private final ReplyPostsRepository replyPostsRepository;

    /**
     * 설명 : 댓글을 저장한다
     * */
    @Transactional
    public Long save(ReplyPostsSaveRequestDto requestDto, User user, Posts posts){
        ReplyPosts savedReplyPosts;

        if (requestDto.getReplyLevel()==0){
            // 첫 댓글: level =0, order= 0 group = 자기댓글id
            ReplyPosts replyPosts = requestDto.toEntity();

            // user 저장
            replyPosts.addUser(user);

            // posts 저장
            replyPosts.setPosts(posts);

            // 댓글 저장
            savedReplyPosts = replyPostsRepository.save(replyPosts);

            // 저장된 댓글 id 를 group id로 저장
            savedReplyPosts.setReplyGroup(savedReplyPosts.getId());

        }else {
            // 대 댓글
            ReplyPosts replyPosts = requestDto.toEntity();

            // user 저장
            replyPosts.addUser(user);

            // posts 저장
            replyPosts.setPosts(posts);

            // 댓글 저장
            savedReplyPosts = replyPostsRepository.save(replyPosts);
        }

        return savedReplyPosts.getId();
    }

    /**
     * 설명 : 댓글을 수정한다
     * */
    @Transactional
    public Long update(ReplyPostsSaveRequestDto requestDto,Long replyId ,User user){

        // 댓글 찾아오기
        ReplyPosts replyPosts = replyPostsRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id: " + replyId));

        // 댓글 수정
        replyPosts.update(requestDto.getContent());


        return replyPosts.getId();
    }


    /**
     * 설명 : 댓글을 삭제한다.
     *      추후 작성자 표시 기능 추가하기 위한 posts 파라미터 요청
     * */
    @Transactional
    public Long delete(Long replyId, Posts posts) {

        // 댓글 찾아오기
        ReplyPosts replyPosts = replyPostsRepository.findById(replyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id: " + replyId));

        //머리 댓글이 삭제되면 아래댓글 다 찾아서 삭제. 댓글 저장 당시 replyGroup을 제일 머리 댓글 id로 설정.
        if (replyPosts.getReplyGroup().equals(replyId)){

            // 같은 group 댓글 전부 찾아오기
            List<ReplyPosts> replyPostsList = replyPostsRepository.findAllByReplyGroup(replyId);

            // 관계 끊어주기 및 삭제
            for (ReplyPosts reply : replyPostsList){
                reply.setPosts(null);
                replyPostsRepository.delete(reply);
            }
        }else {
            // 대댓글일 경우 해당댓글만 삭제
            replyPosts.setPosts(null);
            replyPostsRepository.delete(replyPosts);
        }

        return replyId;
    }

    /**
     * 설명 : 게시글을 삭제할 때 모든 댓글을 같이 삭제한다
     * */
    @Transactional
    public void deleteAll(Long postId) {

        // 해당 게시글 모든 댓글 찾아오기
        List<ReplyPosts> replyPostsList = replyPostsRepository.findAllByPostId(postId);

        // 찾아온 값이 비어있는지 확인
        if (replyPostsList != null && !replyPostsList.isEmpty()) {

            // 비어있지 않다면 삭제
            for (ReplyPosts replyPosts : replyPostsList) {
                replyPosts.setPosts(null);
                replyPostsRepository.delete(replyPosts);
            }
        }
    }

    /**
     * 설명 : 게시글을 상세조회할 때 해당 게시글 댓글을 가져온다
     * */
    @Transactional(readOnly = true)
    public List<ReplyPostsResponseDto> findByPostIdToShow(Long id) {

        // 해당 게시글 모든 댓글 찾아오기
        List<ReplyPosts> replyPostsList = replyPostsRepository.findAllByPostIdAndSort(id);

        // 찾아온 값 넘겨주기
        List<ReplyPostsResponseDto> responseDtos = replyPostsList.stream()
                .map(p -> new ReplyPostsResponseDto(p)).collect(Collectors.toList());

        return responseDtos;
    }
}
