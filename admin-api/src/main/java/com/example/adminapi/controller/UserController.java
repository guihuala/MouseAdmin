package com.example.adminapi.controller;

import com.example.adminapi.entity.User;
import com.example.adminapi.security.JwtUtil;
import com.example.adminapi.security.UserLoginResponse;
import com.example.adminapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("8080")
    private int serverPort;

    @Value("localhost")
    private String serverAddress;
    private static final String DEFAULT_AVATAR_URL = "http://localhost:8080/avatar/default-avatar.png";

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      @RequestParam("email") String email,
                                      @RequestParam("role") String role) {
        if (userService.isUsernameTaken(username)!=null) {
            return ResponseEntity.status(409).body("Username is already taken");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(role);
        user.setAvatar(DEFAULT_AVATAR_URL);

        userService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        UserLoginResponse response = new UserLoginResponse(user, token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) {
            return ResponseEntity.status(401).body("Invalid token or user not authenticated");
        }
        User user = userService.getUserInfo(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("findUsername")
    public ResponseEntity<?> verifyUsernameAndEmail(@RequestParam String username,
                                                    @RequestParam String email) {
        User user = userService.isUsernameTaken(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        if (!user.getEmail().equals(email)) {
            return ResponseEntity.status(400).body("Email does not match");
        }
        return ResponseEntity.ok(user.getUsername());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUserInfo(@PathVariable Long id,
                                            @RequestParam("username") String username,
                                            @RequestParam("email") String email,
                                            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        user.setUsername(username);
        user.setEmail(email);

        if (avatar != null && !avatar.isEmpty()) {
            // 删除旧头像
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                deleteAvatarFile(user.getAvatar());
            }

            // 保存新头像
            String avatarUrl = saveAvatarFile(avatar);
            user.setAvatar(avatarUrl);
        }

        userService.updateUserInfo(id, user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/findPassword")
    public ResponseEntity<?> findPassword(@RequestParam("username") String username,
                                          @RequestParam("email") String email,
                                          @RequestParam("password") String password) {
        User user = userService.isUsernameTaken(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        if (!user.getEmail().equals(email)) {
            return ResponseEntity.status(400).body("Email does not match");
        }
        userService.updatePassword(username, email, password);
        return ResponseEntity.ok("Password updated successfully");
    }

    private String saveAvatarFile(MultipartFile file) {
        // 保存文件并返回文件 URL 的逻辑
        // 例如，将文件保存到本地文件系统或云存储，并返回访问 URL
        // 这里假设文件保存到本地文件系统
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get("src/main/resources/static/avatar", fileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath);
            return String.format("http://%s:%d/avatar/%s", serverAddress, serverPort, fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save avatar file", e);
        }
    }

    private void deleteAvatarFile(String avatarUrl) {
        // 从 URL 中提取文件名
        String fileName = avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
        Path filePath = Paths.get("src/main/resources/static/avatar", fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete avatar file", e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
