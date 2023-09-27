package com.guo.uilts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {

    public static void zipFiles(String dirPath, List<String> fileNames, String zipFilePath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            for (String fileName : fileNames) {
                Path filePath = Paths.get(dirPath, fileName);
                ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
                zos.putNextEntry(zipEntry);
                Files.copy(filePath, zos);
                zos.closeEntry();
            }
        }
    }
}
