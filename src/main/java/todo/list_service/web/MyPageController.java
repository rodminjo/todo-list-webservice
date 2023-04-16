package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todo.list_service.config.oauth.LoginUser;
import todo.list_service.config.oauth.dto.SessionUser;
import todo.list_service.service.user.UserService;
import todo.list_service.web.dto.mypage.MypageIdCheckRequestDto;

@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final UserService userService;


    @PostMapping("/api/v1/mypage/nickname")
    public String checkedId(@RequestBody MypageIdCheckRequestDto requestDto){
        Boolean checkResult = userService.checkedId(requestDto);
        if (checkResult){
            return "available";
        }

        return "notAvailable";
    }

    @PostMapping("/api/v1/mypage/nickname/update")
    public String changeNickName(@RequestBody MypageIdCheckRequestDto requestDto, @LoginUser SessionUser user){
        Boolean checkResult = userService.checkedId(requestDto);

        if (checkResult){
            userService.changeNickName(requestDto, user.getId());
            return "done";
        }

        return "failed";
    }
}
