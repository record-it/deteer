package pl.recordit.deteer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.recordit.deteer.service.FileDocumentService;

@Controller
@RequestMapping("/download")
public class FileDownloadController {
    private final FileDocumentService fileDocumentService;

    @Autowired
    public FileDownloadController(FileDocumentService fileDocumentService) {
        this.fileDocumentService = fileDocumentService;
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileDocumentService.findByFileName(filename);
        if (file != null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }
        return ResponseEntity.notFound().build();
    }
}
