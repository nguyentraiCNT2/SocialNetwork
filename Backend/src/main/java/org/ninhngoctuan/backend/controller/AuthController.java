package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.controller.output.AuthStatus;
import org.ninhngoctuan.backend.dto.EmailActiveDTO;
import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.securityConfig.CustomUserDetailsService;
import org.ninhngoctuan.backend.securityConfig.JwtTokenUtil;
import org.ninhngoctuan.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO dto){
        try {
            UserDTO userDTO =  userService.register(dto);
            userDTO.setPassword(null);
            RequestContext context = RequestContext.get();
            Map<String, Object> response = new HashMap<>();
            if (context != null) {
                response.put("requestId", context.getRequestId());
                response.put("userId", context.getUserId());
                response.put("timestamp", context.getTimestamp());
                response.put("userDetail",userDTO);
            }
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }

    }
    @PostMapping("/active/email")
    public ResponseEntity<?> activeUserEmail(@RequestBody EmailActiveDTO dto){
        try {
            boolean actived =  userService.activeEmail(dto);
            if (actived ==false)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Bạn không thể xác nhận mã xác thực vui lòng thử lại sau"));
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(dto.getEmail());
            RequestContext context = RequestContext.get();
            Map<String, Object> response = new HashMap<>();
            String token = jwtTokenUtil.generateToken(userDetails);
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .maxAge(Duration.ofHours(24))
                    .sameSite("Strict")
                    .secure(true)
                    .path("/")
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            response.put("token", token);
            if (context != null) {
                response.put("requestId", context.getRequestId());
                response.put("userId", context.getUserId());
                response.put("timestamp", context.getTimestamp());
            }
            return ResponseEntity.ok().headers(headers).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }

    }
    @PostMapping("/login")
    public ResponseEntity<?>  login(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            boolean isMatch = BCrypt.checkpw(password, userDetails.getPassword());
            if (!isMatch) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Mật khẩu không chính xác " ));
            }
            RequestContext context = RequestContext.get();
            String token = jwtTokenUtil.generateToken(userDetails);
            Map<String, Object> response = new HashMap<>();
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .maxAge(Duration.ofHours(24))
                    .sameSite("Strict")
                    .secure(true)
                    .path("/")
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            response.put("token", token);
            if (context != null) {
                response.put("requestId", context.getRequestId());
                response.put("userId", context.getUserId());
                response.put("timestamp", context.getTimestamp());
            }
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi không mong muốn: " + e.getMessage()));
        }
    }

    @GetMapping("/auth/status")
    public AuthStatus getAuthStatus() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAuthenticated = !(principal instanceof String && principal.equals("anonymousUser"));

        String role = "";
        if (isAuthenticated) {
            UserDetails userDetails = (UserDetails) principal;
            role = userDetails.getAuthorities().toString();
        }

        return new AuthStatus(isAuthenticated, role);
    }
}
