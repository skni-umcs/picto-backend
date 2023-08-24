package pl.umcs.workshop.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public List<Image> generateImages(int numberOfImages, Long groupId) {
        List<Image> images = imageRepository.findAllByGroupsId(groupId);

        Random rand = new Random();
        List<Image> generatedImages = new ArrayList<>();

        for (int i = 0; i < numberOfImages; i++) {
            Image generatedImage = images.get(rand.nextInt(images.size()));

            if (!generatedImages.contains(generatedImage)) {
                generatedImages.add(generatedImage);
            } else {
                i--;
            }
        }

        return generatedImages;
    }

    public Image generateTopic(List<Image> images) {
        return null;
    }
}
