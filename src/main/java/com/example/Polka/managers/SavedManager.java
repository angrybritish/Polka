package com.example.Polka.managers;

import com.example.Polka.database.Saved;
import com.example.Polka.repository.SavedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavedManager {
    @Autowired
    private static SavedRepository savedRepository;

    public SavedManager(SavedRepository savedRepository) {
        this.savedRepository = savedRepository;
    }
    public static Saved createSaved(Saved saved) {
        return savedRepository.save(saved);
    }

    public static Optional<Saved> getSaved(Long idOwn,Long idBook,String status,Boolean deleted) {
        return savedRepository.findByIdOwnAndIdBookAndStatusAndDeleted(idOwn,idBook,status,deleted);
    }

    public static List<Saved> getMyBooks(Long idOwn, String status, Boolean deleted) {
        return savedRepository.findByIdOwnAndStatusAndDeleted(idOwn,status,deleted);
    }
}
