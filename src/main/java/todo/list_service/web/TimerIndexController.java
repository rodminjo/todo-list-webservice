package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import todo.list_service.config.oauth.LoginUser;
import todo.list_service.config.oauth.dto.SessionUser;
import todo.list_service.service.timer.TimerService;
import todo.list_service.web.dto.timer.TimerResponseDto;

import java.time.LocalDate;

@RequiredArgsConstructor
@Controller
public class TimerIndexController {

    private final TimerService timerService;

    @GetMapping("/timer")
    public String todayInputIndex(){
        return "redirect:/timer/"+ LocalDate.now().toString();
    }

    @GetMapping("/timer/{date}")
    public String timerIndex(@PathVariable String date, Model model, @LoginUser SessionUser user) {
        model.addAttribute("date", date);
        model.addAttribute("timers", timerService.findByUserIdAndTakeAllDesc(date, user.getId()));

        if (user!= null) {
            model.addAttribute("userName", user.getName());
        }
        return "timer-index";
    }


    @GetMapping("/timer/save")
    public String timerSave(@LoginUser SessionUser user ,Model model){
        // 유저이름 입력
        if (user!= null) {
            model.addAttribute("userName", user.getName());
        }
        return "timer-save";
    }

    @GetMapping("/timer/update/{id}")
    public String timerUpdateShow(@PathVariable Long id,@LoginUser SessionUser user ,Model model){
        // 유저이름 입력
        if (user!= null) {
            model.addAttribute("userName", user.getName());
        }

        model.addAttribute("timer", timerService.findById(id));

        return "timer-update";
    }




}
