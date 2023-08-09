package pl.umcs.workshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pl.umcs.workshop.user.User;
import pl.umcs.workshop.user.UserRepository;

import java.util.Optional;

@DataJpaTest
@Rollback(value = false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUserTest() {
        User user = new User(1, 2);

        userRepository.save(user);

        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    @Test
    public void getUserTest() {
        User user = userRepository.findById(1).orElse(null);

        assert user != null;
        Assertions.assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    public void updateUserTest() {
        User user = userRepository.findById(1).orElse(null);

        assert user != null;
        user.setGameId(15);
    }

    @Test
    public void deleteUserTest() {
        User user = userRepository.findById(1).orElse(null);

        userRepository.delete(user);

        User userCheck = null;
        Optional<User> userOptional = userRepository.findById(1);

        if (userOptional.isPresent()) {
            userCheck = userOptional.get();
        }

        Assertions.assertThat(userCheck).isNull();
    }
}
