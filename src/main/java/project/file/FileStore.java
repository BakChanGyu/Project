package project.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    // 학습할 이미지가 저장될 장소
    @Value("${file.compare.dir}")
    private String fileDir;

    // 이미지 여러개 업로드시
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
//        if (multipartFiles.size() != 100) {
//            throw new IOException("100장의 이미지 파일을 올려주세요.");
//        }
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

        File dir = new File(fileDir);
        dir.mkdirs();

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
        return fileDir + filename;
    }
}
