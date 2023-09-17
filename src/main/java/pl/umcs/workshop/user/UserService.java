package pl.umcs.workshop.user;

import java.time.LocalDateTime;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.umcs.workshop.round.RoundService;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;

  public User getUser(Long userId) {
    User user = userRepository.findById(userId).orElse(null);

    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    return user;
  }

  public User updateUser(Long userId, @NotNull User updatedUser) {
    User user = getUser(userId);

    if (!Objects.equals(user.getId(), updatedUser.getId())) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User id's don't match");
    }

    return userRepository.save(updatedUser);
  }

  public User updateUserLastSeen(Long userId) {
    User user = getUser(userId);

    user.setLastSeen(LocalDateTime.now());

    return userRepository.save(user);
  }

  public void deleteUser(Long userId) {
    userRepository.deleteById(userId);
  }

}
