package net.armane.ar9am.controller;

import net.armane.ar9am.repository.TaskRepository;
import net.armane.ar9am.service.ExternalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
public class UploadFile {

    @Autowired
    TaskRepository taskRepository;

    @Value("${image.directory}")
    String directoryOutput;


    @Bean(name = "imageStorage")
    ExternalStorage externalStorage() {
        return ExternalStorage.init("c:/images");
    }

    @PostMapping(path = "/uploadimage")
    public ResponseEntity<String> saveFile(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        File outDir = new File(directoryOutput);

        if (!outDir.exists())
            outDir.mkdir();

        String extension = "";
        if (multipartFile.getOriginalFilename().contains(".")) {
            int i = multipartFile.getOriginalFilename().lastIndexOf('.');
            extension = multipartFile.getOriginalFilename().substring(i);
        }

        String generatedFileName = UUID.randomUUID().toString() + extension;

        File file = new File(outDir, generatedFileName);

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();

        System.out.println(file.getName());

        return ResponseEntity.ok(file.getAbsolutePath());
    }


    @GetMapping(path = "/download")
    public ResponseEntity<Resource> download(@RequestParam("filename") String filename)
            throws IOException {
        File file = new File(directoryOutput, filename);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
