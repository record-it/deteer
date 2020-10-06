package pl.recordit.deteer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.recordit.deteer.dto.FileCategory;
import pl.recordit.deteer.dto.FileDocumentDto;
import pl.recordit.deteer.entity.User;
import pl.recordit.deteer.service.FileDocumentService;
import pl.recordit.deteer.service.ProductService;
import pl.recordit.deteer.storage.StorageFileNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/files")
public class FileUploadController {

    private final FileDocumentService fileDocumentService;
    private final ProductService productService;

    List<FileCategory> allFileCategories() {
        return Arrays.asList(FileCategory.values());
    }

    @Autowired
    public FileUploadController(FileDocumentService fileDocumentService, ProductService productService) {
        this.fileDocumentService = fileDocumentService;
        this.productService = productService;
    }

    @GetMapping("index")
    public String filesIndex(Model model) {
        model.addAttribute("files", fileDocumentService.findAllAsResource().collect(Collectors.toList()));
        model.addAttribute("documents", fileDocumentService.findAll().collect(Collectors.toList()));
        return "files/files";
    }

    @GetMapping("/upload")
    public String uploadFileForm(@ModelAttribute("fileDocumentDto") FileDocumentDto fileDocumentDto, Model model) {
        model.addAttribute("fileCategories", allFileCategories());
        model.addAttribute("products", productService.findAll());
        return "files/uploadFileForm";
    }

    @PostMapping("/upload")
    public ModelAndView handleUploadFile(@ModelAttribute("dto") FileDocumentDto dto, @AuthenticationPrincipal User user) {
        dto.setPublisher(user);
        ModelAndView view = new ModelAndView( fileDocumentService.save(dto)
                .flatMap(f -> Optional.of("redirect:/files/index"))
                .orElse("error"));
        view.getModelMap().clear();
        return view;
    }

    @GetMapping("/upload/manual/{product_id}")
    public String uploadManualForm(@PathVariable long productId, Model model) {
        return productService.findBy(productId)
                .flatMap(product -> {
                    model.addAttribute("product", product);
                    return Optional.of("/files/uploadManualForm");
                })
                .orElse("redirect:/products/index");
    }

    @PostMapping("/upload/manual")
    public String handleUploadManual(@RequestParam(name = "product_id") long product_id, FileDocumentDto dto, @AuthenticationPrincipal User user) {
        dto.setPublisher(user);
        return fileDocumentService.save(dto)
                .flatMap(om -> {
                    productService.updateOperatingManual(product_id, om);
                    return Optional.of("redirect:/products/update/" + product_id);
                })
                .orElse("error");
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
