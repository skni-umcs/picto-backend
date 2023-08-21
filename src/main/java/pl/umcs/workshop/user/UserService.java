package pl.umcs.workshop.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return user;
    }

    // TODO
    public User updateUser(Long userId, User userToUpdate) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (user.getId() != userToUpdate.getId()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User id's don't match");
        }

        // TODO: figure out what methodology we need to update this user
        // e.g. if we need to save field by field or implement operation codes

        return userRepository.save(user);
    }

    // TODO: should we call this level from RoundService to update user
    public void updateUserLastSeen(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        user.setLastSeen(LocalDateTime.now());

        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
