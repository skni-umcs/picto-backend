package pl.umcs.workshop.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUser(int userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        }

        return user;
    }

    // TODO
    public User updateUser(int userId, User userToUpdate) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        }

        if (user.getId() != userToUpdate.getId()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User id's don't match");
        }

        // TODO: figure out what methodology do we need to update this user
        // e.g. if we need to save field by field or implement operation codes

        return userRepository.save(user);
    }

    public User removeUser(int userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        }

        userRepository.delete(user);

        return user;
    }
}
