package todo.list_service.web;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import todo.list_service.config.oauth.LoginUser;
import todo.list_service.config.oauth.dto.SessionUser;

import todo.list_service.service.todo.TodoService;
import todo.list_service.web.dto.todo.TodoRequestimportanceCheckedDto;
import todo.list_service.web.dto.todo.TodoSaveRequestDto;
import todo.list_service.web.dto.todo.TodoUpdateRequestDto;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class TodoApiController {

    private final TodoService todoService;

    /**
     *  설명 : 저장 페이지에서 저장버튼 클릭시 이미지(multipart), 데이터(json) 을 받아 저장한다.
     *      전달값 : UserId, title, content, todoDate, files
     *      반환 : todoId
     * */

    @PostMapping("/api/v1/todo")
    public Long save(@RequestPart(value = "key") TodoSaveRequestDto requestDto,
                     @RequestPart(value = "file", required = false) List<MultipartFile> files,
                     @LoginUser SessionUser user) throws Exception {

        // DTO에 userId 입력
        requestDto.setUserId(user.getId());

        // DTO, file을 전달하여 사진저장
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
