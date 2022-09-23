package project.file;

import lombok.Data;

@Data
public class UploadFile {

    private String uploadFileName; // 업로드된 파일이름
    private String storeFileName; // 저장된 파일이름. 덮어쓰기 방지를위함.

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

}
