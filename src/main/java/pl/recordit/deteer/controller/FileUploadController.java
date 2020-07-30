package pl.recordit.deteer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.recordit.deteer.dto.FileDocumentDto;
import pl.recordit.deteer.service.FileDocumentService;
import pl.recordit.deteer.service.ProductService;
import pl.recordit.deteer.storage.StorageFileNotFoundException;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/files")
public class FileUploadController {

    private final FileDocumentService fileDocumentService;
    private final ProductService productService;

    @Autowired
    public FileUploadController(FileDocumentService fileDocumentService, ProductService productService) {
        this.fileDocumentService = fileDocumentService;
        this.productService = productService;
    }

    @GetMapping("index")
    public String filesIndex(Model model) {
        model.addAttribute("files", fileDocumentService.findAllAsResource().collect(Collectors.toList()));
        return "files/files";
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileDocumentService.findByFileName(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/upload")
    public String uploadFileForm(@ModelAttribute("fileDocumentDto") FileDocumentDto fileDocumentDto, Model model){
        model.addAttribute("products", productService.findAll());
        return "files/uploadFileForm";
    }

    @PostMapping("/upload")
    public String handleUploadFile(@ModelAttribute("dto") FileDocumentDto dto){
        return fileDocumentService.save(dto).flatMap(f -> Optional.of("redirect:/files/all")).orElse("error");
    }

    @GetMapping("/upload/manual/{product_id}")
    public String uploadManualForm(@PathVariable long product_id, Model model) {
        return productService.findBy(product_id)
                .flatMap(product -> {
                            model.addAttribute("product", product);
                            return Optional.of("/file/uploadManualForm");
                        }
                ).orElse("redirect:/products/index");
    }

    @PostMapping("/upload/manual")
    public String handleUploadManual(@RequestParam(name = "product_id") long product_id, FileDocumentDto dto, Model model) {
        return fileDocumentService.save(dto)
                .flatMap(om -> {
                    productService.updateOperatingManual(product_id, om);
                    return Optional.of("redirect:/products/update/"+product_id);
                })
                .orElse("error");
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
