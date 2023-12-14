package com.example.Polka.managers;

import com.example.Polka.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Polka.database.File;
import com.example.Polka.repository.FileRepository;

import java.util.List;
import java.util.Optional;
@Service
public class FileManager {
    @Autowired
    private static FileRepository fileRepository;

    public FileManager(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public static File createFile(File file) {
        return fileRepository.save(file);
    }

    public static Optional<File> readAllById(Long id) {
        return fileRepository.findAllById(id);
    }

    public static Optional<File> getFileById(Long id) {
        return fileRepository.findById(id);
    }
}
