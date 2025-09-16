package geadezest.service;

import geadezest.entity.Photo;
import geadezest.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    public Photo downloadImageById(Integer id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
    }
}
