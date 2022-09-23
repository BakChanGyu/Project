package project.file;

import lombok.Data;

import java.util.List;

@Data
public class Item {

    private Long itemId;
    private List<UploadFile> imageFiles;
}
