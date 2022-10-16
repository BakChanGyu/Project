package project.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemForm {

    private Integer itemId; // 1이면 경찰, 2면 수능감독관, 3이면 토익감독관, 4면 비회원
    private List<MultipartFile> imageFiles;
}
