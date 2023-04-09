package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import todo.list_service.config.auth.LoginUser;
import todo.list_service.config.auth.dto.SessionUser;
import todo.list_service.service.todo.TodoService;
import todo.list_service.web.dto.todo.TodoRequestimportanceCheckedDto;
import todo.list_service.web.dto.todo.TodoSaveRequestDto;
import todo.list_service.web.dto.todo.TodoUpdateRequestDto;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class TodoApiController {

    private final TodoService todoService;


    @PostMapping("/api/v1/todo")
    public Long save(@RequestPart(value = "key") TodoSaveRequestDto requestDto,
                     @RequestPart(value = "file", required = false) List<MultipartFile> files,
                     @LoginUser SessionUser user) throws Exception {
        if (user != null) {
            requestDto.setUserId(user.getId());
        }
        System.out.println("requestDto = " + requestDto);
        System.out.println("files = " + files);

        return todoService.save(requestDto, files);
    }

    @PostMapping("/api/v1/todo/{id}")
    public Long update(@PathVariable Long id,
                       @RequestPart(value = "key") TodoUpdateRequestDto requestDto,
                       @RequestPart(value = "file", required = false)List<MultipartFile> files) throws Exception {

        System.out.println("requestDto = " + requestDto);
        System.out.println("files = " + files);
        System.out.println("id = " + id);

        requestDto.setTodoId(id);

        return todoService.update(requestDto, files);
    }


    @DeleteMapping("/api/v1/todo/{id}")
    public Long delete(@PathVariable Long id){
        String todoDate = todoService.delete(id);
        return id;
    }

    @PostMapping("/api/v1/todo/importance/{id}")
    public Long importanceChange(@PathVariable Long id, @RequestBody TodoRequestimportanceCheckedDto requestDto){
        requestDto.setId(id);
        System.out.println("requestDto = " + requestDto.getImportance());
        return todoService.importanceChange(requestDto);
    }

    @PostMapping("/api/v1/todo/checked/{id}")
    public Long checkedChange(@PathVariable Long id, @RequestBody TodoRequestimportanceCheckedDto requestDto){
        requestDto.setId(id);
        System.out.println("requestDto = " + requestDto.getChecked());
        return todoService.checkedChange(requestDto);
    }


}
