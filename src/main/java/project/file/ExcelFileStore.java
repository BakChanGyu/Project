package project.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class ExcelFileStore {

    // 파일 저장장소
    @Value("${excel.dir}")
    private String fileDir;

    private String folderPath;
    // 이미지 여러개 업로드시
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, String memberType) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        log.error("들어오는파일 ={}", multipartFiles);

        createFolder(memberType);

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }

        return storeFileResult;
    }

    // 파일 업로드
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        // 서버에 저장하는 파일명
        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFileName, storeFileName);
    }

    private String createStoreFileName(String originalFileName) {
        // 서버에 저장하는 파일명
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        // 확장자 남겨두기
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        // 원래파일의 . 뒤에 오는 위치에 확장자명 저장.
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

    // 서버에 저장될 파일 경로 + 이름
    private String getFullPath(String filename) {
        return folderPath + "/" + filename;
    }

    private void createFolder(String memberType) {
        String targetName;

        switch (memberType) {
            case "police":
                targetName = "missing";
                break;
            case "teacher":
                targetName = "csat";
            default:
                targetName = "toeic";
        }

        // 해당 폴더 없을경우 생성
        folderPath = fileDir + "/" + targetName + "/excel";
        File Folder = new File(folderPath);
        if (!Folder.exists()) {
            Folder.mkdirs();
        }
    }
}
