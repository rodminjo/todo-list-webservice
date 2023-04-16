package todo.list_service.web;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import todo.list_service.config.oauth.LoginUser;
import todo.list_service.config.oauth.dto.SessionUser;

import todo.list_service.service.todo.TodoService;

import java.time.LocalDate;


@RequiredArgsConstructor
@Controller
public class TodoIndexController {

    private final TodoService todoService;

    /**
     * 설명 : todo 게시판으로 이동했을때 오늘 날짜에 저장된 목록을 가져와서 출력한다
     **/
    @GetMapping("/todo")
    public String todayInputIndex(){

        return "redirect:/todo/"+LocalDate.now().toString();

    }

    /**
     * 설명 : todo 게시판에서 날짜를 선택하면 해당 날짜에 저장된 목록을 가져와서 출력한다
     *      반환값: id, title, checked, todoDate, importance, storedFileName
     **/
    @GetMapping("/todo/{date}")
    public String todoIndex(@PathVariable String date, Model model, @LoginUser SessionUser user) {

        // 유저 닉네임 정보 넘겨주기 위함
        if (user!= null) {
            model.addAttribute("userName", user.getNickName());
        }

        // 선택한 날짜에 맞는 할일 목록을 가져옴
        model.addAttribute("todos", todoService.findByUserIdAndTakeAllDesc(date, user.getId()));

        return "todo-index";
    }


    /**
     *  설명 : todo 게시판에서 할일등록 버튼을 클릭하면 저장 화면으로 이동한다
     * */
    @GetMapping("/todo/save")
    public String todayInputTodoSave(){

        return "redirect:/todo/save/" + LocalDate.now().toString();

    }

    /**
     *  설명 : 날짜를 선택한 상태에서 할일등록 버튼을 클릭하면 해당 날짜 저장화면으로 이동한다
     * */
    @GetMapping("/todo/save/{date}")
    public String todoSave(@PathVariable String date, @LoginUser SessionUser user, Model model){

        // 유저 닉네임 정보 넘겨주기 위함
        if (user!= null) {
            model.addAttribute("userName", user.getNickName());
        }

        // 선택되어 있는 날짜 정보 넘겨주기 위함
        model.addAttribute("date", date);

        return "todo-save";
    }

    /**
     *  설명 : todo 게시판에서 todo를 클릭하면 해당 todo를 수정, 삭제할 수 있는 상세정보로 이동
     * */
    @GetMapping("/todo/update/{id}")
    public String todoUpdateShow(@PathVariable Long id, @LoginUser SessionUser user, Model model){

        // 유저 닉네임 정보 넘겨주기 위함
        if (user!= null) {
            model.addAttribute("userName", user.getNickName());
        }

        // 상세 정보 반환
        model.addAttribute("todo", todoService.findByIdToShow(id));


        return "todo-update";
    }





}
