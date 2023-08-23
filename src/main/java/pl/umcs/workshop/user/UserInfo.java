package pl.umcs.workshop.user;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    private Long userId;
    private Long roundId;
    private int answerTime;
    private Long imageSelected;
    private List<Long> symbolsSelected;
}
