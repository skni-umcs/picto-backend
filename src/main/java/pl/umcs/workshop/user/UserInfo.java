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
    private Long userId;
    private Long roundId;
    private int answerTime;
    private Long imageSelected;
    private List<Long> symbolsSelected;
}
