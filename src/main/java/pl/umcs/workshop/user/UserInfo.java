package pl.umcs.workshop.user;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// TODO: this as a parent and SpeakerInfo and ListenerInfo children?
public class UserInfo {
    private int userId;
    private int roundId;
    private int answerTime;
    private int imageSelected;
    private List<Integer> symbolsSelected;
}
