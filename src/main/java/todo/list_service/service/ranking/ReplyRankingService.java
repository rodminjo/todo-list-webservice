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

    @Transactional
    public Long save(ReplyRankingSaveRequestDto requestDto, User user, Ranking ranking) {
        ReplyRanking replyRanking = ReplyRanking.builder()
                .user(user)
                .replyContent(requestDto.getContent()).build();
        replyRanking.setRanking(ranking);

        return replyRankingRepository.save(replyRanking).getId();

    }

    @Transactional
    public Long update(ReplyRankingSaveRequestDto requestDto, Long replyId, User user) {
        ReplyRanking replyRanking = replyRankingRepository.findById(replyId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 없습니다. id : " + replyId));

        replyRanking.update(requestDto);

        return replyRanking.getId();
    }

    @Transactional
    public Long delete(Long replyId, Ranking ranking) {
        ReplyRanking replyRanking = replyRankingRepository.findById(replyId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 없습니다. id : " + replyId));

        replyRanking.setRanking(null);
        replyRankingRepository.delete(replyRanking);

        return replyId;
    }

    @Transactional
    public void deleteAll(Long rankingId) {
        List<ReplyRanking> replyRankingList = replyRankingRepository.findAllByRankingId(rankingId);
        if (replyRankingList != null && !replyRankingList.isEmpty()){
            for (ReplyRanking replyRanking : replyRankingList){
                replyRanking.setRanking(null);
                replyRankingRepository.delete(replyRanking);
            }
        }
    }

}
