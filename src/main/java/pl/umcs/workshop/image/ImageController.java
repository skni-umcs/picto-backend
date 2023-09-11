package pl.umcs.workshop.image;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("image/")
public class ImageController {
  @Autowired private ImageService imageService;

  @PostMapping("add")
  public void addImages() {
    imageService.addImages();
    imageService.addSymbols();
  }

  @GetMapping("all")
  public List<Image> getAllImagesWithGroups() {
    return imageService.getAllImagesWithGroups();
  }
}
