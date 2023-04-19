package todo.list_service.service.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.list_service.domain.ranking.Ranking;
import todo.list_service.domain.replyranking.ReplyRanking;
import todo.list_service.domain.replyranking.ReplyRankingRepository;
import todo.list_service.domain.user.User;
import todo.list_service.web.dto.ranking.ReplyRankingSaveRequestDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyRankingService {
    private final ReplyRankingRepository replyRankingRepository;

    /**
     * 설명 : Ranking 댓글을 저장한다
     * */
    @Transactional
    public Long save(ReplyRankingSaveRequestDto requestDto, User user, Ranking ranking) {

        // DTO -> Reply 객체 생성
        ReplyRanking replyRanking = requestDto.toEntity(user);

        // Reply 객체에 ranking 저장
        replyRanking.setRanking(ranking);

        // 저장후 저장한 객체 id 출력
        return replyRankingRepository.save(replyRanking).getId();

    }

    /**
     * 설명 : Ranking 댓글을 수정한다
     * */
    @Transactional
    public Long update(ReplyRankingSaveRequestDto requestDto, Long replyId, User user) {

        // 댓글 찾아오기
        ReplyRanking replyRanking = replyRankingRepository.findById(replyId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 없습니다. id : " + replyId));

        // 댓글 수정
        replyRanking.update(requestDto);

        // 수정 댓글 id 반환
        return replyRanking.getId();
    }

    /**
     * Ranking 댓글을 삭제한다
     * */
    @Transactional
    public Long delete(Long replyId, Ranking ranking) {

        // 댓글 찾아오기
        ReplyRanking replyRanking = replyRankingRepository.findById(replyId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 없습니다. id : " + replyId));

        // 댓글 참조 관계 끊기
        replyRanking.setRanking(null);

        // 댓글 삭제
        replyRankingRepository.delete(replyRanking);

        return replyId;
    }

    @Transactional
    public void deleteAll(Long rankingId) {

        // 댓글 목록 찾아오기
        List<ReplyRanking> replyRankingList = replyRankingRepository.findAllByRankingId(rankingId);

        // 비어있는지 확인
        if (replyRankingList != null && !replyRankingList.isEmpty()){

            // 비어있지 않다면 for 문을 이용하여 관계 끊고 삭제
            for (ReplyRanking replyRanking : replyRankingList){

                replyRanking.setRanking(null);
                replyRankingRepository.delete(replyRanking);
            }
        }
    }

}
