package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import todo.list_service.config.auth.LoginUser;
import todo.list_service.config.auth.dto.SessionUser;
import todo.list_service.service.todo.TodoService;
import todo.list_service.web.dto.todo.TodoResponseDto;

import java.time.LocalDate;


@Controller
@RequiredArgsConstructor
public class TodoIndexController {

    private final TodoService todoService;


    @GetMapping("/todo")
    public String todayInputIndex(){
        return "redirect:/todo/"+LocalDate.now().toString();
    }

    @GetMapping("/todo/{date}")
    public String todoIndex(@PathVariable String date, Model model, @LoginUser SessionUser user) {
        model.addAttribute("date", date);

        if (user!= null) {
            model.addAttribute("todos", todoService.findByUserIdAndTakeAllDesc(date, user.getId()));
            model.addAttribute("userName", user.getName());

        }
        return "todo-index";
    }

    @GetMapping("/todo/save")
    public String todayInputTodoSave(){
        return "redirect:/todo/save/" + LocalDate.now().toString();
    }

    @GetMapping("/todo/save/{date}")
    public String todoSave(@PathVariable String date, Model model){
        if (date != null){
            model.addAttribute("date", date);
        }
        return "todo-save";
    }

    @GetMapping("/todo/update/{id}")
    public String todoUpdateShow(@PathVariable Long id,@LoginUser SessionUser user, Model model){
        TodoResponseDto responseDto = todoService.findByIdToShow(id);
        model.addAttribute("todo", responseDto);
        if (user!= null) {
            model.addAttribute("userName", user.getName());
        }

        return "todo-update";
    }





}
