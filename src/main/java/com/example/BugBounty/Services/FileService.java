package com.example.BugBounty.Services;

import org.springframework.stereotype.Service;
import java.io.File;
import java.util.*;

@Service
public class FileService {

    /**
     * Fetches the folder structure of the given directory.
     *
     * @param directoryPath The path of the directory to read.
     * @return A list representing the file structure.
     */
    public List<Map<String, Object>> getFolderStructure(String directoryPath) {
        File folder = new File(directoryPath);
        return folder.exists() ? getFilesRecursive(folder, directoryPath) : new ArrayList<>();
    }

    /**
     * Recursively fetches the file and folder structure.
     *
     * @param folder   The current folder to process.
     * @param basePath The base directory path for relative paths.
     * @return A list of maps containing file/folder details.
     */
    private List<Map<String, Object>> getFilesRecursive(File folder, String basePath) {
        List<Map<String, Object>> fileList = new ArrayList<>();
        File[] files = folder.listFiles();

        if (files != null) {
            Arrays.sort(files); // Sort files alphabetically

            for (File file : files) {
                Map<String, Object> fileData = new HashMap<>();
                fileData.put("name", file.getName());
                fileData.put("relativePath", file.getAbsolutePath().replace(basePath, ""));
                fileData.put("isDirectory", file.isDirectory());

                if (file.isDirectory()) {
                    fileData.put("children", getFilesRecursive(file, basePath)); // Recursive call for subfolders
                } else {
                    fileData.put("size", file.length()); // Add file size
                }

                fileList.add(fileData);
            }
        }
        return fileList;
    }
}
