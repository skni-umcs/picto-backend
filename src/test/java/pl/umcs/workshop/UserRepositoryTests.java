package pl.umcs.workshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;

import java.time.LocalDateTime;
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
                .gameId(1)
                .score(6)
                .lastSeen(LocalDateTime.now())
                .cookie("Hello guys :D")
                .build();

        userRepository.save(user);

        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    @Test
    @Order(value = 2)
    public void getUserTest() {
        User user = userRepository.findById(1).orElse(null);

        assert user != null;
        Assertions.assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    @Order(value = 3)
    public void updateUserTest() {
        User user = userRepository.findById(1).orElse(null);

        assert user != null;
        user.setGameId(15);
    }

    @Test
    @Order(value = 4)
    public void deleteUserTest() {
        User user = userRepository.findById(1).orElse(null);

        assert user != null;
        userRepository.delete(user);

        User userCheck = null;
        Optional<User> userOptional = userRepository.findById(1);

        if (userOptional.isPresent()) {
            userCheck = userOptional.get();
        }

        Assertions.assertThat(userCheck).isNull();
    }
}
