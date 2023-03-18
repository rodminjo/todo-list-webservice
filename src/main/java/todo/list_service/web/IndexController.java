package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import todo.list_service.config.auth.LoginUser;
import todo.list_service.config.auth.dto.SessionUser;
import todo.list_service.service.posts.PostsService;
import todo.list_service.web.dto.PostsResponseDto;


@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());
        if (user!= null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save/")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}/")
    public String postsUpdateShow(@PathVariable Long id, Model model){
        PostsResponseDto dto =postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
