package project.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    // 파일을 서버에 업로드
    @PostMapping("/upload")
    public void saveImage(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        log.info("storeImageFiles={}", storeImageFiles);
        Item item = new Item();
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getItemId());
    }
}
