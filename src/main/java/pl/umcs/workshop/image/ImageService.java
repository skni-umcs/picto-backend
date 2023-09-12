package pl.umcs.workshop.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.umcs.workshop.game.Game;
import pl.umcs.workshop.group.Group;
import pl.umcs.workshop.group.GroupRepository;
import pl.umcs.workshop.relation.ImageUserRoundRelation;
import pl.umcs.workshop.relation.ImageUserRoundRelationRepository;
import pl.umcs.workshop.round.Round;
import pl.umcs.workshop.round.RoundRepository;
import pl.umcs.workshop.symbol.Symbol;
import pl.umcs.workshop.symbol.SymbolRepository;
import pl.umcs.workshop.user.User;

@Service
public class ImageService {
  @Autowired private ImageRepository imageRepository;

  @Autowired private SymbolRepository symbolRepository;

  @Autowired private RoundRepository roundRepository;

  @Autowired private GroupRepository groupRepository;

  @Autowired private ImageUserRoundRelationRepository imageUserRoundRelationRepository;

  private static double getRandomized(int numberOfImages) {
    int mean = numberOfImages / 2;
    int variance = numberOfImages / 10;
    Random rand = new Random();

    double randomized = rand.nextGaussian() * variance + mean;

    return Math.atan(randomized);
  }

  private static int getIndex(int numberOfImages, double x0) {
    int k = 1;
    while (x0 > ((double) k / numberOfImages)) {
      k++;
    }

    return k - 2;
  }

  public @NotNull List<Image> generateImagesForRoundForUser(Long groupId) {
    List<Image> images = imageRepository.findAllByGroupsId(groupId);
    List<Image> roundImages = new ArrayList<>();

    for (int i = 0; i < images.size(); i++) {
      int index = getIndex(images.size(), getRandomized(images.size()));
      Image generatedImage = images.get(index);

      roundImages.add(generatedImage);
      images.remove(generatedImage);
    }

    return roundImages;
  }

  public void generateImagesForGame(Game game) {
    List<Round> rounds = roundRepository.findAllByGame(game);

    for (Round round : rounds) {
      for (User user : new User[] {round.getUserOne(), round.getUserTwo()}) {
        List<Image> images = generateImagesForRoundForUser(game.getGroup().getId());
        Image topic = getTopic(images);

        for (Image image : images) {
          ImageUserRoundRelation imageUserRoundRelation =
              ImageUserRoundRelation.builder().round(round).user(user).image(image).build();

          imageUserRoundRelationRepository.save(imageUserRoundRelation);
        }

        round.setTopic(topic);
        roundRepository.save(round);
      }
    }
  }

  public Image getTopic(@NotNull List<Image> userImages) {
    return userImages.get(getIndex(userImages.size(), getRandomized(userImages.size())));
  }

  public void addImages() {
    Set<String> images = listFiles("src/main/resources/static");

    Group group = Group.builder().name("Please don't sell my wife").build();
    group = groupRepository.save(group);

    for (String imagePath : images) {
      Image image =
          Image.builder()
              .path(imagePath)
              .groups(new HashSet<>(Collections.singleton(group)))
              .build();
      imageRepository.save(image);
    }
  }

  public void addSymbols() {
    Set<String> symbols = listFiles("src/main/resources/static");

    Group group = Group.builder().name("Please don't sell my wife").build();
    group = groupRepository.save(group);

    for (String symbolPath : symbols) {
      Symbol symbol = Symbol.builder().path(symbolPath).group(group).build();
      symbolRepository.save(symbol);
    }
  }

  public Set<String> listFiles(String dir) {
    try (Stream<Path> stream = Files.list(Paths.get(dir))) {
      return stream
          .filter(file -> !Files.isDirectory(file))
          .map(Path::getFileName)
          .map(Path::toString)
          .collect(Collectors.toSet());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Image> getAllImagesWithGroups() {
    return imageRepository.findAll();
  }
}
