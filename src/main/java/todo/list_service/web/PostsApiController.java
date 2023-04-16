package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import todo.list_service.config.oauth.LoginUser;
import todo.list_service.config.oauth.dto.SessionUser;
import todo.list_service.service.posts.PostsService;
import todo.list_service.web.dto.posts.PostsSaveRequestDto;
import todo.list_service.web.dto.posts.PostsUpdateRequestDto;
import todo.list_service.web.dto.posts.ReplyPostsSaveRequestDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;


    @PostMapping("/api/v1/posts")
    public Long save(@RequestPart(value = "key") PostsSaveRequestDto requestDto,
                     @RequestPart(value = "file", required = false) List<MultipartFile> files,
                     @LoginUser SessionUser user) throws Exception {
        if (user != null) {
            requestDto.setUserId(user.getId());
        }

        return postsService.save(requestDto, files);
    }

    @PostMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id,
                       @RequestPart(value ="key") PostsUpdateRequestDto requestDto,
                       @RequestPart(value = "file", required = false) List<MultipartFile> files
                        ) throws Exception {

        requestDto.setPostId(id);

        return postsService.update(requestDto, files);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id){

        return postsService.delete(id);
    }

    @PostMapping("/api/v1/posts/{id}/reply")
    public Long saveReply(@PathVariable Long id,
                          @RequestBody ReplyPostsSaveRequestDto requestDto,
                          @LoginUser SessionUser user) {

        return postsService.replySave(requestDto, id, user.getId());
    }

    @PostMapping("/api/v1/posts/{id}/reply/{replyId}")
    public Long updateReply(@PathVariable Long id,
                          @PathVariable Long replyId,
                          @RequestBody ReplyPostsSaveRequestDto requestDto,
                          @LoginUser SessionUser user) {
        //ReplyPostsUpdateDto 고려하기

        return postsService.replyUpdate(requestDto, id ,replyId, user.getId());
    }

    @DeleteMapping("/api/v1/posts/{id}/reply/{replyId}")
    public Long deleteReply(@PathVariable Long id,
                            @PathVariable Long replyId,
                            @LoginUser SessionUser user){

        return postsService.replyDelete(id, replyId, user.getId());
    }
}
