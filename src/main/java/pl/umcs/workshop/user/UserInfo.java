package pl.umcs.workshop.user;

import java.util.List;
import lombok.*;
import pl.umcs.workshop.image.Image;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
  private Long userId;
  private Long roundId;
  private int answerTime;
  private Image imageSelected;
  private List<Image> symbolsSelected;
}
