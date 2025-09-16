package geadezest.controller;

import geadezest.entity.Photo;
import geadezest.service.ImageUtils;
import geadezest.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping("/{id}")
    @Operation(summary = "Id buyicha imageni olish")
    public ResponseEntity<Resource> downloadImage(@PathVariable Integer id) {
        Photo image = photoService.downloadImageById(id);

        byte[] decompressed = ImageUtils.decompressImage(image.getData());
        ByteArrayResource resource = new ByteArrayResource(decompressed);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .body(resource);
    }
}
