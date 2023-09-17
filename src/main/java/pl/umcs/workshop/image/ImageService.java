package pl.umcs.workshop.image;

import java.io.File;
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

  private static double getRandomizedNormal(double mean, double variance) {
    Random rand = new Random();
    double randomized = rand.nextGaussian() * variance + mean;

    return Math.atan(randomized) / (Math.PI / 2);
  }

  private static int getRandomized(int numberOfImages) {
    Random rand = new Random();

    return rand.nextInt(numberOfImages);
  }

  private static int getIndex(int numberOfImages, double x0) {
    int k = 1;
    while (x0 > ((double) k / numberOfImages)) {
      k++;
    }

    return k - 1;
  }

  public static Set<String> listFiles(String dir) {
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

  public @NotNull List<Image> generateImagesForRoundForUser(Long groupId) {
    List<Image> images = imageRepository.findAllByGroupId(groupId);
    List<Image> roundImages = new ArrayList<>();

    for (int i = 0; i < images.size(); i++) {
      int index = getIndex(images.size(), getRandomizedNormal(0, 1));
      Image generatedImage = images.get(index);

      roundImages.add(generatedImage);
      images.remove(generatedImage);
    }

    return roundImages;
  }

  public void generateImagesForGame(Game game) {
    List<Round> rounds = roundRepository.findAllByGame(game);

    List<ImageUserRoundRelation> relations = new ArrayList<>();
    List<Round> roundsToSave = new ArrayList<>();

    for (Round round : rounds) {
      List<Image> images = generateImagesForRoundForUser(game.getGroup().getId());
      Image topic = getTopic(images);

      for (Image image : images) {
        for (User user : new User[] {round.getUserOne(), round.getUserTwo()}) {
          ImageUserRoundRelation imageUserRoundRelation =
              ImageUserRoundRelation.builder().round(round).user(user).image(image).build();

          relations.add(imageUserRoundRelation);
        }
      }

      round.setTopic(topic);
      roundsToSave.add(round);
    }

    imageUserRoundRelationRepository.saveAll(relations);
    roundRepository.saveAll(roundsToSave);
  }

  public Image getTopic(@NotNull List<Image> userImages) {
    return userImages.get(getRandomized(userImages.size()));
  }

  public void addImages() {
    File file = new File("src/main/resources/static/images");
    String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

    assert directories != null;
    for (String directory : directories) {
      Set<String> images = listFiles("src/main/resources/static/images/" + directory);

      Group group = Group.builder().name(directory).type("image").build();
      group = groupRepository.save(group);

      for (String imagePath : images) {
        Image image = Image.builder().path(imagePath).group(group).build();
        imageRepository.save(image);
      }
    }
  }

  public void addSymbols() {
    File file = new File("src/main/resources/static/symbols");
    String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

    assert directories != null;
    for (String directory : directories) {
      Set<String> symbols = listFiles("src/main/resources/static/symbols/" + directory);

      Group group = Group.builder().name(directory).type("symbol").build();
      group = groupRepository.save(group);

      for (String symbolPath : symbols) {
        Symbol symbol =
            Symbol.builder()
                .path("symbols/" + group.getName() + "/" + symbolPath)
                .group(group)
                .build();
        symbolRepository.save(symbol);
      }
    }
  }

  public List<Image> getAllImagesAndGroups() {
    return imageRepository.findAll();
  }

  public List<Group> getAllImageGroups() {
    return groupRepository.findAllByType("image");
  }
}
