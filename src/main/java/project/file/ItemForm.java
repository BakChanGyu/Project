package project.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemForm {

    private Integer itemId;
    private String idCode;
    private List<MultipartFile> imageFiles;
}
