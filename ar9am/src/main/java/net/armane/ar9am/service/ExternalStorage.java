package net.armane.ar9am.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class ExternalStorage {

    public static String outputDirectory;

    public static ExternalStorage init(String directoryOutput) {
        outputDirectory = directoryOutput;
        return new ExternalStorage();
    }

    private ExternalStorage() {

    }

    public File saveRandomly(MultipartFile multipartFile, String... prefix) throws Exception {

        String extension = "";
        if (multipartFile.getOriginalFilename().contains(".")) {
            int i = multipartFile.getOriginalFilename().lastIndexOf('.');
            extension = multipartFile.getOriginalFilename().substring(i);
        }
        String generatedFileName = UUID.randomUUID().toString() + extension;

        File file = new File(outputDirectory, generatedFileName);

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();

        return file;
    }

    public File readFile(String name) {
        return new File(outputDirectory, name);
    }


}
