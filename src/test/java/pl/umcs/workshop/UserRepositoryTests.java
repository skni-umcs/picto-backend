package pl.umcs.workshop;

import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pl.umcs.workshop.topology.Topology;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;
import pl.umcs.workshop.utils.JWTCookieHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@Rollback(value = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(value = 1)
    public void saveUserTest() {
        User user = User.builder()
                .gameId(1L)
                .score(6)
                .lastSeen(LocalDateTime.now())
                .cookie(JWTCookieHandler.createToken(1L, 1L))
                .build();

        userRepository.save(user);

        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    @Test
    @Order(value = 2)
    public void getUserTest() {
        User user = userRepository.findById(1L).orElse(null);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    @Order(value = 3)
    public void getListOfAllUsersTest() {
        User user = User.builder()
                .gameId(2L)
                .score(11)
                .lastSeen(LocalDateTime.now())
                .cookie(JWTCookieHandler.createToken(2L, 2L))
                .build();

        userRepository.save(user);

        List<User> users = userRepository.findAll();

        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @Order(value = 4)
    public void getListOfAllUsersByGameIdTest() {
        User user = User.builder()
                .id(3L)
                .gameId(1L)
                .score(17)
                .lastSeen(LocalDateTime.now())
                .cookie(JWTCookieHandler.createToken(3L, 3L))
                .build();

        userRepository.save(user);

        List<User> users = userRepository.findAllByGameId(1L);

        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @Order(value = 5)
    public void updateUserTest() {
        User user = userRepository.findById(3L).orElse(null);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getScore()).isEqualTo(17);

        user.setScore(user.getScore() + -2);

        User savedUser = userRepository.save(user);

        Assertions.assertThat(user.getScore()).isEqualTo(15);
        Assertions.assertThat(savedUser.getScore()).isEqualTo(15);
        Assertions.assertThat(user.getId()).isEqualTo(savedUser.getId());
    }

    @Test
    @Order(value = 6)
    public void deleteUserTest() {
        User user = userRepository.findById(1L).orElse(null);

        Assertions.assertThat(user).isNotNull();
        userRepository.delete(user);

        User userCheck = null;
        Optional<User> userOptional = userRepository.findById(1L);

        if (userOptional.isPresent()) {
            userCheck = userOptional.get();
        }

        Assertions.assertThat(userCheck).isNull();
    }
}
