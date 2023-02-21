package project.file;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.ai.compare.CsatAIService;
import project.ai.compare.MissingAIService;
import project.ai.compare.ToeicAIService;
import project.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UploadFileController {
    private final FileStore fileStore;
    private final ExcelFileStore excelFileStore;
    private final MissingAIService missingAIService;
    private final CsatAIService csatAIService;
    private final ToeicAIService toeicAIService;

    // 이미지 캡처 전 저장될 경로
    @Value("${delete.dir}")
    private String dir;

    @Value("${delete.new.dir}")
    private String learnDir;

    // 엑셀 데이터 저장 후 지워주기 위한 변수
    @Value("${excel.dir}")
    private String excelPath;

    // 비교할 이미지 업로드
    @PostMapping("/image/missing/upload")
    public ResponseEntity<?> saveMissingImage(@ModelAttribute ItemForm form, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession(false);
        log.info("회원 session ={}", session);
        // 비회원 비교할 이미지 업로드시
        if(session == null) {
            return new ResponseEntity<>("비회원입니다.", HttpStatus.BAD_REQUEST);
        }

        if(form.getImageFiles() == null) {
            return new ResponseEntity<>("이미지파일이 null 입니다!", HttpStatus.BAD_REQUEST);
        }

        if(form.getImageFiles().size() != 1) {
            return new ResponseEntity<>("1장의 필적을 넣어주세요!", HttpStatus.BAD_REQUEST);
        }

        // 해당하는 memberType만 사용 가능하게 변경
        Member member = (Member) session.getAttribute("login-member");
        String memberType = member.getMemberType();
        if (!memberType.equals("police")) {
            return new ResponseEntity<>("해당 필적 비교는 경찰만 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        // 기존 비교할 필적이 저장된 폴더 초기화 후 시작. 사용 전 파일삭제

        delDir(dir);

        // 캡처 후 폴더 초기화
//        delDir(learnDir);

        // 서버에 이미지 저장
        try {
            fileStore.storeFiles(form.getImageFiles());
        } catch (IOException e) {
            return new ResponseEntity<>("IOException 발생!" + e, HttpStatus.BAD_REQUEST);
//            return new ResponseEntity<>("error_code: 100장의 이미지 파일을 올려주세요.", HttpStatus.OK);
        }

        // 이미지 캡쳐 api 호출
        missingAIService.imgCap();

        // AI를 통해 필적 비교 - api 호출
        try {
            JsonObject jsonObject = missingAIService.requestToFlask();
            log.info("반환값 ={}", jsonObject);
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("해당 값이 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/image/csat/upload")
    public ResponseEntity<?> saveCsatImage(@ModelAttribute ItemForm form, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession(false);
        log.info("회원 session ={}", session);
        // 비회원 비교할 이미지 업로드시
        if(session == null) {
            return new ResponseEntity<>("비회원입니다.", HttpStatus.BAD_REQUEST);
        }
        if(form.getImageFiles() == null) {
            return new ResponseEntity<>("파일이 null입니다.", HttpStatus.BAD_REQUEST);
        }
        if(form.getImageFiles().size() != 1) {
            return new ResponseEntity<>("1장의 필적을 넣어주세요!", HttpStatus.BAD_REQUEST);
        }

        // 해당하는 memberType만 사용 가능하게 변경
        Member member = (Member) session.getAttribute("login-member");
        String memberType = member.getMemberType();
        if (!memberType.equals("teacher")) {
            return new ResponseEntity<>("해당 필적 비교는 감독관만 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        // 기존 비교할 필적이 저장된 폴더 초기화 후 시작. 사용 전 파일삭제
        delDir(dir);

        // 캡처 후 폴더 초기화
//        delDir(learnDir);

        // 서버에 이미지 저장
        try {
            fileStore.storeFiles(form.getImageFiles());
        } catch (IOException e) {
            return new ResponseEntity<>("IOException 발생!" + e, HttpStatus.BAD_REQUEST);
//            return new ResponseEntity<>("error_code: 100장의 이미지 파일을 올려주세요.", HttpStatus.OK);
        }

        // 이미지 캡쳐 api 호출
        csatAIService.imgCap();

        // AI를 통해 필적 비교
        try {
            JsonObject jsonObject = csatAIService.requestToFlask();
            log.info("반환값 ={}", jsonObject);
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("해당 값이 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/image/toeic/upload")
    public ResponseEntity<?> saveToeicImage(@ModelAttribute ItemForm form, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession(false);
        log.info("회원 session ={}", session);
        // 비회원 비교할 이미지 업로드시
        if(session == null) {
            return new ResponseEntity<>("비회원입니다.", HttpStatus.BAD_REQUEST);
        }
        if(form.getImageFiles() == null) {
            return new ResponseEntity<>("파일이 null입니다.", HttpStatus.BAD_REQUEST);
        }
        if(form.getImageFiles().size() != 1) {
            return new ResponseEntity<>("1장의 필적을 넣어주세요!", HttpStatus.BAD_REQUEST);
        }

        // 해당하는 memberType만 사용 가능하게 변경
        Member member = (Member) session.getAttribute("login-member");
        String memberType = member.getMemberType();
        if (!memberType.equals("supervisor")) {
            return new ResponseEntity<>("해당 필적 비교는 감독관만 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        // 기존 비교할 필적이 저장된 폴더 초기화 후 시작. 사용 전 파일삭제
        delDir(dir);

        // 캡처 후 폴더 초기화
//        delDir(learnDir);

        // 서버에 이미지 저장
        try {
            fileStore.storeFiles(form.getImageFiles());
        } catch (IOException e) {
            return new ResponseEntity<>("IOException 발생!" + e, HttpStatus.BAD_REQUEST);
//            return new ResponseEntity<>("error_code: 100장의 이미지 파일을 올려주세요.", HttpStatus.OK);
        }

        // 이미지 캡쳐 api 호출
        toeicAIService.imgCap();

        // AI를 통해 필적 비교
        try {
            JsonObject jsonObject = toeicAIService.requestToFlask();
            log.info("반환값 ={}", jsonObject);
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("해당 값이 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/excel/upload")
    public ResponseEntity<?> saveExcelFile(@ModelAttribute ItemForm form, HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        log.info("회원 session ={}", session);
        
        if(session == null) {
            return new ResponseEntity<>("비회원입니다.", HttpStatus.BAD_REQUEST);
        }
        if(form.getImageFiles() == null) {
            return new ResponseEntity<>("파일이 null입니다.", HttpStatus.BAD_REQUEST);
        }
        if(form.getImageFiles().size() != 1) {
            return new ResponseEntity<>("1장의 엑셀 파일을 넣어주세요!", HttpStatus.BAD_REQUEST);
        }

        // 회원 종류에 따라 다른 폴더에 저장
        // 세션에서 memberType 불러와서 해당 이름에 맞게 경로 추가.
        Member member = (Member) session.getAttribute("login-member");
        log.info("로그인한멤버정보 ={}", member);
        if (member == null) {
            return null;
        }

        String memberType = member.getMemberType();

        // 서버에 파일 저장
        try {
            excelFileStore.storeFiles(form.getImageFiles(), memberType);
            if (memberType.equals("police")) {

                excelPath += "/missing/excel";
                // 엑셀에서 데이터 추출하는 api 호출
                missingAIService.saveExcel();
            } else if (memberType.equals("teacher")) {

                excelPath += "/cast/excel";
                // 엑셀에서 데이터 추출하는 api
                csatAIService.saveExcel();
            } else {

                excelPath += "/toeic/excel";
                // 엑셀에서 데이터 추출하는 api
                toeicAIService.saveExcel();
            }
            return new ResponseEntity<>("엑셀 파일 저장 완료.", HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>("IOException 발생. 업로드 실패!", HttpStatus.BAD_REQUEST);
        } finally {
            // 엑셀은 저장하고나서 지우기로
            delDir(excelPath);
        }
    }

    private void delDir(String path) {
        // 파일 clear
        DeleteFile deleteLearnFile = new DeleteFile();
        deleteLearnFile.deleteFile(path);
    }
}
