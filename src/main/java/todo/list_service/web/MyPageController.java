package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todo.list_service.config.oauth.LoginUser;
import todo.list_service.config.oauth.dto.SessionUser;

import todo.list_service.service.user.UserService;

import todo.list_service.web.dto.mypage.MypageIdCheckRequestDto;

/**
 * 설명 : 마이페이지에서 실행할 API CONTROLLER
 * */
@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final UserService userService;

    /**
     * 설명 : 닉네임 중복여부를 판별하고 결과를 반환한다.
     * */
    @PostMapping("/api/v1/mypage/nickname")
    public String checkedId(@RequestBody MypageIdCheckRequestDto requestDto){

        // 닉네임 중복여부
        Boolean checkResult = userService.checkedId(requestDto);

        // 중복여부에 따른 반환
        if (checkResult){
            return "available";
        }

        return "notAvailable";
    }


    /**
     * 설명 : 닉네임 중복여부를 판별하고 닉네임을 변경한다.
    * */
    @PostMapping("/api/v1/mypage/nickname/update")
    public String changeNickName(@RequestBody MypageIdCheckRequestDto requestDto, @LoginUser SessionUser user){

        // 닉네임 중복여부
        Boolean checkResult = userService.checkedId(requestDto);

        // 닉네임 중복이 아니라면 변경하고 결과 반환
        if (checkResult){

            userService.changeNickName(requestDto, user.getId());

            return "done";
        }

        return "failed";
    }
}
