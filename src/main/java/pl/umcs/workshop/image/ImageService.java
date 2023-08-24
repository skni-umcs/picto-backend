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

        List<Image> generatedImages = new ArrayList<>();

        for (int i = 0; i < numberOfImages; i++) {
            Image generatedImage = images.get(getRandomized(numberOfImages));

            generatedImages.add(generatedImage);
            images.remove(generatedImage);
        }

        return generatedImages;
    }

    public Image generateTopic(Long groupId) {
        List<Image> images = imageRepository.findAllByGroupsId(groupId);

        return images.get(getRandomized(1));
    }


    private static int getRandomized(int numberOfImages) {
        Random rand = new Random();

        int mean = numberOfImages / 2;
        int variance = numberOfImages / 10;

        double randomized = rand.nextGaussian() * variance + mean;
        while (randomized < 0 || randomized > numberOfImages) {
            randomized = rand.nextGaussian() * variance + mean;
        }

        return (int) randomized;
    }
}
