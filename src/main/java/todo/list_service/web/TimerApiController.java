package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import todo.list_service.config.auth.LoginUser;
import todo.list_service.config.auth.dto.SessionUser;
import todo.list_service.service.timer.TimerService;
import todo.list_service.web.dto.timer.TimerSaveRequestDto;
import todo.list_service.web.dto.timer.TimerUpdateRequestDto;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class TimerApiController {

    private final TimerService timerService;

    @PostMapping("/api/v1/timer")
    public Long timerSave(@RequestBody TimerSaveRequestDto requestDto, @LoginUser SessionUser user){
        if (user != null){
            requestDto.setUserId(user.getId());
        }
        requestDto.setTodoDate(LocalDate.now().toString());

        return timerService.save(requestDto);
    }

    @PostMapping("/api/v1/timer/{id}")
    public Long timerUpdate(@RequestBody TimerUpdateRequestDto requestDto, @PathVariable Long id){
        requestDto.setId(id);
        return timerService.update(requestDto);
    }

    @DeleteMapping("/api/v1/timer/{id}")
    public Long timerDelete(@PathVariable Long id){

        return timerService.delete(id);
    }





}
