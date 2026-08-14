package be.kdg.programming5.onepiece.presentation.controller;

import be.kdg.programming5.onepiece.business.service.CsvImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final CsvImportService csvImportService;

    public AdminController(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @GetMapping("/upload")
    public String showUploadForm() {
        return "admin/uploadCharacters";
    }

    @PostMapping("/upload")
    public String uploadCharacters(@RequestParam("file") MultipartFile file, Principal principal,
                                   RedirectAttributes redirectAttributes) throws IOException {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please choose a CSV file.");
            return "redirect:/admin/upload";
        }

        List<String> dataLines;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            dataLines = reader.lines().skip(1).filter(line -> !line.isBlank()).toList();
        }

        logger.debug("Starting async import of {} row(s), uploaded by {}", dataLines.size(), principal.getName());
        csvImportService.importCharacters(dataLines, principal.getName());

        redirectAttributes.addFlashAttribute("successMessage",
                "Import started for " + dataLines.size() + " row(s). Characters will appear shortly.");
        return "redirect:/admin/upload";
    }
}
