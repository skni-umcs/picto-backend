package pl.umcs.workshop.image;

import org.springframework.beans.factory.annotation.Autowired;
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
  }
}
