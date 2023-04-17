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

    /**
     * 설명 : 수정 화면에서 정보를 수정한다
     *      전달값 : todoId, title, content, todoDate
     *      반환값 : todoId
     * */

    @PostMapping("/api/v1/todo/{id}")
    public Long update(@PathVariable Long id,
                       @RequestPart(value = "key") TodoUpdateRequestDto requestDto,
                       @RequestPart(value = "file", required = false)List<MultipartFile> files) throws Exception {


        requestDto.setTodoId(id);

        return todoService.update(requestDto, files);
    }


    /**
     * 설명 : todo id를 받아 삭제한다.
     *      전달값 : todoId
     *      반환값 : 삭제된 id
     * */

    @DeleteMapping("/api/v1/todo/{id}")
    public Long delete(@PathVariable Long id){

        String todoDate = todoService.delete(id);

        return id;
    }

    /**
     * 설명 : 메인페이지에서 중요도를 설정할때 저장한다
     * */

    @PostMapping("/api/v1/todo/importance/{id}")
    public Long importanceChange(@PathVariable Long id, @RequestBody TodoRequestimportanceCheckedDto requestDto){

        requestDto.setId(id);

        return todoService.importanceChange(requestDto);
    }

    /**
     * 설명 : 메인페이지에서 완료여부를 설정할 때 저장한다.
     * */

    @PostMapping("/api/v1/todo/checked/{id}")
    public Long checkedChange(@PathVariable Long id, @RequestBody TodoRequestimportanceCheckedDto requestDto){

        requestDto.setId(id);

        return todoService.checkedChange(requestDto);
    }


}
