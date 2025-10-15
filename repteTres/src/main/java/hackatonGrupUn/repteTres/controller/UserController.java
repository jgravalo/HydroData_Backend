package hackatonGrupUn.repteTres.controller;

import hackatonGrupUn.repteTres.dto.LoginRequestDto;
import hackatonGrupUn.repteTres.model.User;
import hackatonGrupUn.repteTres.service.AuthService;
import hackatonGrupUn.repteTres.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto request) {
        boolean valid = authService.login(request.getUsername(), request.getPassword());
        if (valid) {
            return ResponseEntity.ok("Successfully logged in");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}