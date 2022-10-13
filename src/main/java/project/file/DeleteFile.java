package project.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class DeleteFile {

//    @Test
//    public void test() {
//        deleteFile("E:/study/test");
//    }

    public void deleteFile (String filepath) {
        File targetFolder = new File(filepath);

        if(!targetFolder.exists()) {
            log.info("{} >>> 경로가 존재하지 않습니다.", targetFolder);
            return;
        }

        File[] files = targetFolder.listFiles();
        log.info("파일 리스트 ={}", (Object) files);

        for (File file : files) {
            if (file.delete()) {
                log.info("{} - 파일 삭제 완료", file);
            } else {
                log.info("{} - 파일 삭제 실패", file);
                return;
            }
        }

        log.info("{} - 폴더 전체 파일 삭제 완료", targetFolder);
    }
}
