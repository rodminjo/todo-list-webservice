package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import todo.list_service.config.oauth.LoginUser;
import todo.list_service.config.oauth.dto.SessionUser;
import todo.list_service.service.posts.PostsService;
import todo.list_service.web.dto.posts.PostsResponseDto;

@Controller
@RequiredArgsConstructor
public class PostsIndexController {
    private final PostsService postsService;

    /**
     * 설명 : 게시글 인덱스 페이지 목록조회
     * */
    @GetMapping("/posts")
    public String index(@RequestParam(value = "search", required = false) String search,
                        @RequestParam(value ="sort", required = false) Integer sort,
                        Model model,
                        @LoginUser SessionUser user) {
        // 로그인 정보 조회
        if (user!= null) {
            model.addAttribute("userName", user.getName());
        }

        // 선택한 정렬 select에 넣어주기
        if (sort!=null) {
            model.addAttribute("select", sort);
        }

        // 검색에 따른 정렬 조회
        if (search == null){
            model.addAttribute("posts", postsService.findAll(sort));
        }else{
            model.addAttribute("posts", postsService.findBySearch(search,sort));
        }

        return "posts-index";
    }

    /**
     * 설명 : 게시글 저장 페이지 이동
     * */
    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }


    /**
     * 설명 : 게시글 상세조회 및 수정페이지 이동
     * */
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


        return "posts-update";
    }



}
