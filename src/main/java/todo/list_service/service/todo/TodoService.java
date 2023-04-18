package todo.list_service.service.todo;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import todo.list_service.domain.todo.Todo;
import todo.list_service.domain.user.User;

import todo.list_service.domain.todo.TodoRepository;
import todo.list_service.service.upload.UploadService;
import todo.list_service.service.user.UserService;

import todo.list_service.web.dto.todo.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserService userService;
    private final UploadService uploadService;


    /**
     * 설명 : todo 를 저장한다
     * */
    @Transactional
    public Long save(TodoSaveRequestDto requestDto, List<MultipartFile> files) throws Exception {

        // User 불러오기
        User user = userService.findById(requestDto.getUserId());

        // 저장할 Todo 생성
        Todo todo = requestDto.toEntity();

        // 저장할 todo에 user 저장
        todo.userAdd(user);

        // todo 저장
        Todo savedTodo = todoRepository.save(todo);

        //file uploadTodo에 저장
        uploadService.todoSave(files,savedTodo);


        return savedTodo.getId();
    }


    /**
     * 설명 : todo 를 수정한다
     *      전달값 : title, content, todoDate, file
     * */
    @Transactional
    public Long update(TodoUpdateRequestDto requestDto, List<MultipartFile> files) throws Exception {

        // todo 객체 불러오기
        Todo savedTodo = todoRepository.findById(requestDto.getTodoId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다"));

        //todo 정보 업데이트
        savedTodo.update(requestDto.getTitle(),requestDto.getContent(), requestDto.getTodoDate());

        // 파일이 존재하는지 확인
        if (files == null || files.isEmpty()) {
            // 요청 파일이 없을때 전부 삭제
            uploadService.todoDelete(savedTodo.getId());
        }else {
            // 요청파일이 있을때 삭제
            uploadService.todoDelete(savedTodo.getId());

            //새로운 요청으로 저장
            uploadService.todoSave(files, savedTodo);
        }

        return savedTodo.getId();
    }

    /**
     *  설명 : Todo 메인페이지에 표시될 날짜에 맞는 데이터 출력
     **/
    @Transactional(readOnly = true)
    public List<TodosListResponseDto> findByUserIdAndTakeAllDesc(String date, Long id){

        // 회원 id와 선택 날짜를 받아 해당하는 데이터 리스트 반환
        List<TodosListResponseDto> listDto = todoRepository.findByUserIdAndAllDesc(date, id).stream()
                .map(todos -> new TodosListResponseDto(todos)).collect(Collectors.toList());

        // todo id를 받아 해당 데이터에 맞는 썸네일용 이미지 데이터 DTO에 추가
        for (TodosListResponseDto list : listDto) {
            String thumbnailStoredFileName = uploadService.FindThumbnailByTodoId(list.getId());

            if (thumbnailStoredFileName !=null){
                list.addStoredFileName(thumbnailStoredFileName);
            }
        }

        return listDto;
    }


    /**
     * 설명 : todo id 를 받아 상세 화면에 출력될 데이터 반환
     * */
    @Transactional(readOnly = true)
    public TodoResponseDto findByIdToShow(Long id){
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id: " + id));

        return new TodoResponseDto(todo);
    }


    /**
     * 설명 : todo id를 받아 데이터 삭세
     * */
    @Transactional
    public String delete(Long id) {
        //Todo 객체 찾아오기
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다"));
        // 날짜 데이터 반환을 위한 저장
        String deleteTodoDate = todo.getTodoDate();

        // 이미지 삭제
        uploadService.todoDelete(todo.getId());

        // 글 삭제
        todoRepository.delete(todo);

        return deleteTodoDate;
    }


    /**
     * 설명 : todo 중요도를 변경
     * */
    public Long importanceChange(TodoRequestimportanceCheckedDto requestImportanceDto) {

        // todo 찾아오기
        Todo todo = todoRepository.findById(requestImportanceDto.getId()).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다"));

        // todo 중요도 변경
        todo.importanceChange(requestImportanceDto.getImportance());

        return todo.getId();
    }

    /**
     * 설명 : todo 완료여부를 변경
     * */
    public Long checkedChange(TodoRequestimportanceCheckedDto requestImportanceDto) {

        // todo 찾아오기
        Todo todo = todoRepository.findById(requestImportanceDto.getId()).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다"));

        // todo 완료여부 변경
        todo.checkedChange(requestImportanceDto.getChecked());

        return todo.getId();
    }


}
