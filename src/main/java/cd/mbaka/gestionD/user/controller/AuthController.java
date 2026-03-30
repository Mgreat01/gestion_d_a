package cd.mbaka.gestionD.user.controller;

import cd.mbaka.gestionD.user.model.UserModel;
import cd.mbaka.gestionD.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@RequestBody UserModel user) {
        return ResponseEntity.ok(userService.register(user));
    }


    // AuthController.java

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        String token = userService.login(email, password);
        UserModel user = userService.findByEmail(email);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getFullName());

        return ResponseEntity.ok(response);
    }
}