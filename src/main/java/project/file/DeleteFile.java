package project.file;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

@Slf4j
public class DeleteFile {

    @Test
    public void test() {
        deleteFile("E:/study/test");
    }

    public boolean deleteFile (String filepath) {
        File targetFolder = new File(filepath);

        if(!targetFolder.exists()) {
            log.info("{} >>> 경로가 존재하지 않습니다.", targetFolder);
            return false;
        }

        File[] files = targetFolder.listFiles();

        for (File file : files) {
            if (file.delete()) {
                log.info("{} - 파일 삭제 완료", file);
            } else {
                log.info("{} - 파일 삭제 실패", file);
                return false;
            }
        }

        log.info("{} - 폴더 전체 파일 삭제 완료", targetFolder);
        return true;
    }
}
