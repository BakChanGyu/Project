package project.missing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.repository.MemberRepository;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class MissingController {

    private final MissingService missingService;
    private final MemberRepository memberRepository;

    @Autowired
    public MissingController(MissingService missingService, @Qualifier("MissingMemberRepository") MemberRepository memberRepository) {
        this.missingService = missingService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {

        // 실종자 리스트 반환
        List<MissingMember> missingMember = missingService.list();

        // 리스트 조회 실패시, 에러반환
        if (missingMember == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 리스트 조회 성공시 listMember 반환
        log.info("missingMember={}", missingMember);
        return new ResponseEntity<>(missingMember, HttpStatus.OK);
    }

    @PostMapping("/missing_add")
    public ResponseEntity<?> add(@RequestBody MissingMember member, BindingResult bindingResult) {
        // 에러가 있는 경우 다시 회원가입 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        memberRepository.save(member);
        log.info("회원가입완료 member={}", member);
        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @PostMapping("/missing_delete")
    public ResponseEntity<?> delete(@RequestBody MissingMember member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        memberRepository.delete(member);
        log.info("회원삭제완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/missing_update")
    public ResponseEntity<?> update(@RequestBody MissingMember member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        memberRepository.updateByMissingcode(member);
        log.info("업데이트완료 member={}", member);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    /**
     * TODO :
     * 예외처리 (중복체크, 검증 등)
     */

}
