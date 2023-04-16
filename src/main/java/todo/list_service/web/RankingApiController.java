package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import todo.list_service.config.oauth.LoginUser;
import todo.list_service.config.oauth.dto.SessionUser;
import todo.list_service.service.ranking.RankingService;
import todo.list_service.web.dto.ranking.ReplyRankingSaveRequestDto;

@RequiredArgsConstructor
@RestController
public class RankingApiController {
    private final RankingService rankingService;

    @PostMapping("/api/v1/ranking/{id}/reply")
    public Long saveReply(@PathVariable Long id, @RequestBody ReplyRankingSaveRequestDto requestDto, @LoginUser SessionUser user){
        return rankingService.replySave(requestDto, id, user.getId());
    }

    @PostMapping("/api/v1/ranking/{id}/reply/{replyId}")
    public Long updateReply(@PathVariable Long id, @PathVariable Long replyId,
                            @RequestBody ReplyRankingSaveRequestDto requestDto,
                            @LoginUser SessionUser user){
        //ReplyRankingUpdateDto 고려하기

        return rankingService.replyUpdate(requestDto, id, replyId, user.getId());
    }

    @DeleteMapping("/api/v1/ranking/{id}/reply/{replyId}")
    public Long deleteReply(@PathVariable Long id,
                            @PathVariable Long replyId,
                            @LoginUser SessionUser user){
        return rankingService.replyDelete(id, replyId, user.getId());
    }

}
