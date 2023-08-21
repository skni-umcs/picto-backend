package pl.umcs.workshop.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("{userId}")
    public User getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    // TODO
    @PutMapping("{userId}")
    public User updateUser(@PathVariable int userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }
}
