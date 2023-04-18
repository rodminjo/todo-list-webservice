package todo.list_service.service.upload;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import todo.list_service.domain.uploadTodo.UploadTodo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UploadTodoHandler {
    public List<UploadTodo> parseFileInfo(List<MultipartFile> multipartFiles) throws Exception{
        // 반환 파일 리스트
        List<UploadTodo> fileList = new ArrayList<>();

        // 빈 파일이 들어오면 실행 안됨 (multipartFiles.isEmpty() 써도 되지만 null일때 오류가 발생할 수도 있어서 아래방법 사용)
        if (!CollectionUtils.isEmpty(multipartFiles)){

            //파일 이름을 업로드 한 날짜로 바꾸어 저장
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String currentDate = simpleDateFormat.format(new Date());

            //프로젝트 폴더에 저장하기 위해 절대경로 설정, File.separator는 경로구분자로 윈도우와 맥에서 구분자가 다른것을 해결하기 위해 사용
            String absolutePath = new File("").getAbsolutePath() + File.separator+File.separator;

            //경로 지정하고 그곳에 저장
            String path = "images" + File.separator + currentDate;
            File file = new File(path);

            // 저장할 위치에 디렉토리가 존재하지 않으면
            if(!file.exists()) {
                // mkdirs : 상위 디렉토리까지 생성, mkdir: 해당디렉토리만 생성
                boolean wasSuccessful = file.mkdirs();


                if (!wasSuccessful) {
                    System.out.println("파일 디렉토리 생성에 실패하였습니다.");
                }
            }
            // 파일 만지기
            for (MultipartFile multipartFile : multipartFiles){

                // JPEG, PNG, GIF 만 받음. 확장자 추출
                String contentType = multipartFile.getContentType();
                String originalFileExtension;

                // 확장자명이 없으면 잘못된것
                if (ObjectUtils.isEmpty(contentType)){
                    System.out.println("확장자명이 없습니다.");
                    break;
                }else {
                    if (contentType.contains("image/jpeg")){
                        originalFileExtension = ".jpg";
                    }else if (contentType.contains("image/png")){
                        originalFileExtension = ".png";
                    } else if (contentType.contains("image/gif")){
                        originalFileExtension = ".gif";
                    } else{
                        System.out.println("사진파일(jpeg,png,gif)이 아닙니다 : " + contentType);
                        break;
                    }
                }
                // 이름을 나노초까지
                String newFileName = System.nanoTime() + originalFileExtension;

                // 파일 리스트에 추가
                UploadTodo upload = UploadTodo.builder()
                        .originalFileName(multipartFile.getOriginalFilename())
                        .storedFileName(path + File.separator + newFileName)
                        .fileSize(multipartFile.getSize())
                        .build();
                fileList.add(upload);

                // 업로드한 파일 데이터를 지정한 파일에 저장
                file = new File(absolutePath + path + File.separator + newFileName);
                multipartFile.transferTo(file);

                // 파일 권한 설정
                file.setWritable(true);
                file.setReadable(true);

            }
        }


        return fileList;
    }

    public void delete(String storedFileName) {
        String absolutePath = new File("").getAbsolutePath() + File.separator+File.separator;

        try{
            Path file = Paths.get(absolutePath+storedFileName);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.out.println("파일 삭제에 실패하였습니다. "+e.getMessage());
        }
    }
}
