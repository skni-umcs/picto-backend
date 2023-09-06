package pl.umcs.workshop.image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.relation.ImageUserRoundRelation;
import pl.umcs.workshop.relation.ImageUserRoundRelationRepository;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundRepository;
import pl.umcs.workshop.user.User;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private ImageUserRoundRelationRepository imageUserRoundRelationRepository;

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

    public List<Image> generateImagesForRoundForUser(Long groupId, int numberOfImages) {
        List<Image> images = imageRepository.findAllByGroupsId(groupId);
        List<Image> roundImages = new ArrayList<>();

        for (int i = 0; i < numberOfImages; i++) {
            Image generatedImage = images.get(getRandomized(numberOfImages));

            roundImages.add(generatedImage);
            images.remove(generatedImage);
        }

        return roundImages;
    }

    public void generateImagesForGame(Game game) {
        List<Round> rounds = roundRepository.findAllByGame(game);

        for (Round round : rounds) {
            for (User user : new User[]{round.getUserOne(), round.getUserTwo()}) {
                int numberOfImages = user.equals(round.getUserOne()) ? game.getUserOneNumberOfImages() : game.getUserTwoNumberOfImages();
                List<Image> images = generateImagesForRoundForUser(game.getGroup().getId(), numberOfImages);
                Image topic = getTopic(images);

                for (Image image : images) {
                    ImageUserRoundRelation imageUserRoundRelation =
                            ImageUserRoundRelation.builder()
                                .round(round)
                                .user(user)
                                .image(image)
                                .build();

                    imageUserRoundRelationRepository.save(imageUserRoundRelation);
                }

                round.setTopic(topic);
                roundRepository.save(round);
            }
        }
    }

    public Image getTopic(@NotNull List<Image> userImages) {
        return userImages.get(getRandomized(userImages.size()));
    }

    // save List<ImageUserRoundRelation> for given game

    // getTopic(List<Image> userImages);
}
