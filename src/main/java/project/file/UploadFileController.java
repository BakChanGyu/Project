package project.file;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.AI.AIService;
import project.repository.ItemRepository;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UploadFileController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;
    private final AIService aiService;

    // 파일을 서버에 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> saveImage(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        try {
            List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
            log.info("storeImageFiles={}", storeImageFiles);

            Item item = new Item();
            item.setImageFiles(storeImageFiles);
            itemRepository.save(item);

            redirectAttributes.addAttribute("itemId", item.getItemId());
        } catch (IOException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }

        JsonObject jsonObject = aiService.requestToFlask(form.getItemId());

        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }
}
