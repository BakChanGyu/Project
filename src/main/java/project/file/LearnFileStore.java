package project.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.member.Member;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LearnFileStore {

    // 학습할 이미지가 저장될 장소
    @Value("${file.learn.dir}")
    private String fileDir;
    private String folderPath;

    // 이미지 여러개 업로드시
    public List<UploadFile> storeLearnFiles(List<MultipartFile> multipartFiles, String idCode, HttpSession session) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        log.error("들어오는파일 ={}", multipartFiles);
        // 세션에서 memberType 불러와서 해당 이름에 맞게 경로 추가.
        Member member = (Member) session.getAttribute("login-member");
        log.info("로그인한멤버정보 ={}", member);
        if (member == null) {
            return null;
        }
        String memberType = member.getMemberType();
        createFolder(idCode, memberType);

        int index = 1;
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, idCode, index++));
            }
        }
        return storeFileResult;
    }

    private UploadFile storeFile(MultipartFile multipartFile, String idCode, int index) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        // 서버에 저장하는 파일명
        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName, idCode, index);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFileName, storeFileName);
    }

    private String createStoreFileName(String originalFileName, String idCode, int index) {
        // 서버에 저장하는 파일명
        String ext = extractExt(originalFileName);

        // 확장자 남겨두기
        return idCode + "(" + index + ")." + ext;
    }

    private String extractExt(String originalFileName) {
        // 원래파일의 . 뒤에 오는 위치에 확장자명 저장.
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

    private String getFullPath(String filename) {
        return folderPath + "/" + filename;
    }

    private void createFolder(String idCode, String memberType) {
        String targetName;

        switch (memberType) {
            case "police":
                targetName = "missing";
                break;
            case "teacher":
                targetName = "csat";
                break;
            default:
                targetName = "toeic";
        }

        // 해당 폴더 없을경우 생성
        folderPath = fileDir + "/" + targetName + "/imgcap/original/" + idCode;
        File Folder = new File(folderPath);
        if (!Folder.exists()) {
            Folder.mkdirs();
        }
    }
}

