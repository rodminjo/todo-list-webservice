package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import todo.list_service.config.auth.LoginUser;
import todo.list_service.config.auth.dto.SessionUser;
import todo.list_service.service.posts.PostsService;
import todo.list_service.web.dto.posts.PostsResponseDto;

@Controller
@RequiredArgsConstructor
public class PostsIndexController {
    private final PostsService postsService;
    @GetMapping("/posts")
    public String index(@RequestParam(value = "search", required = false) String search,
                        @RequestParam(value ="sort", required = false) Integer sort,
                        Model model,
                        @LoginUser SessionUser user) {
        if (user!= null) {
            model.addAttribute("userName", user.getName());
        }
        if (sort!=null) {
            model.addAttribute("select", sort);
        }
        // post.id, title, author, viewCount, createdDate, storedFileName
        if (search == null){
            model.addAttribute("posts", postsService.findAll(sort));
        }else{
            model.addAttribute("posts", postsService.findBySearch(search,sort));
        }

        return "posts-index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdateShow(@PathVariable Long id, Model model,@LoginUser SessionUser user){
        // 게시글 정보 가져오기
        PostsResponseDto dto =postsService.findByIdToShow(id);

        if (user!= null) {
            model.addAttribute("userName", user.getName());
            model.addAttribute("userPicture", user.getPicture());
            model.addAttribute("userId", user.getId());
        }

        // 정보 모델에 담기
        model.addAttribute("posts", dto);
        // 게시글id : post.id
        // 게시글제목, 내용 : post.title, post.content
        // 게시글사진 리스트 : post.storedFileNames
        // 댓글 정보 리스트 : post.replyDtos
        // 댓글 id : post.replyDtos.id
        // 댓글 닉네임, 사진, 내용  : post.replyDtos.nickName , picture, content
        // 댓글 생성일(LocalDateTime) : post.replyDtos.createTime
        // 댓글 level : post.replyDtos.level


        return "posts-update";
    }



}
