package geadezest.controller;
import geadezest.payload.ApiResponse;
import geadezest.payload.auth.AuthLogin;
import geadezest.payload.auth.AuthRegister;
import geadezest.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody AuthRegister register){
        ApiResponse register1 = authService.register(register);
        return ResponseEntity.status(register1.getStatus()).body(register1);
    }

    @PostMapping("/check-code")
    public ResponseEntity<ApiResponse> check_code(@RequestParam String email, @RequestParam Integer code) {
        ApiResponse check = authService.check_code(email, code);
        return ResponseEntity.status(check.getStatus()).body(check);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthLogin login) {
        ApiResponse login1 = authService.login(login);
        return ResponseEntity.status(login1.getStatus()).body(login1);
    }

}
