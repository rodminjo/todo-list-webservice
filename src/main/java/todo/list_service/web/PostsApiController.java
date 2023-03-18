package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import todo.list_service.service.posts.PostsService;
import todo.list_service.web.dto.PostsResponseDto;
import todo.list_service.web.dto.PostsSaveRequestDto;
import todo.list_service.web.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts/")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }

    @PostMapping("/api/v1/posts/{id}/")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}/")
    public PostsResponseDto findById(@PathVariable Long id){
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}/")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }
}
