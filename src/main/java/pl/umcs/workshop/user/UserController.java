package pl.umcs.workshop.user;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private UserService userService;

    @GetMapping("user/{userId}")
    public User getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    // TODO
    @PostMapping("user/update/{userId}")
    public User updateUser(@PathVariable int userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @PostMapping("user/remove/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }
}
